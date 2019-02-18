package io.soabase.structured.logger.generation;

import io.soabase.structured.logger.LoggingFormatter;
import io.soabase.structured.logger.exception.InvalidSchemaException;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bytebuddy.implementation.MethodCall.invoke;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class Generator {
    private final Map<Class, Entry> generated = new ConcurrentHashMap<>();
    private static final Set<String> reservedMethodNames = Collections.unmodifiableSet(
            Stream.of(Instance.class.getMethods()).map(Method::getName).collect(Collectors.toSet())
    );
    private static final Method setValueMethod;
    static {
        try {
            setValueMethod = Instance.class.getMethod("slogSetValue", String.class, Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find method slogSetValue()", e);
        }
    }

    private interface Applicator {
        void apply(String mainMessage, Map<String, Object> values, Throwable t, BiConsumer<String, Object[]> appliedConsumer, boolean requireAllSchemaMethods);
    }

    private static class Entry<T> implements Generated<T>, Applicator {
        volatile Class<T> generated;
        volatile Applicator applicator;

        @Override
        public Class<T> generated() {
            return generated;
        }

        @Override
        public void apply(String mainMessage, Map<String, Object> values, Throwable t, BiConsumer<String, Object[]> appliedConsumer, boolean requireAllSchemaMethods) {
            applicator.apply(mainMessage, values, t, appliedConsumer, requireAllSchemaMethods);
        }
    }

    public void clearCache() {
        generated.clear();
    }

    @SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
    public <T> Generated<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter) {
        Entry<T> newEntry = new Entry<>();
        Entry<T> existingEntry = generated.putIfAbsent(schemaClass, newEntry);
        final Entry useEntry = (existingEntry != null) ? existingEntry : newEntry;
        if (useEntry.generated == null) {
            boolean hasCustom = validateSchemaClass(schemaClass);
            synchronized (useEntry) {
                if (useEntry.generated == null) {
                    List<String> names = Stream.of(schemaClass.getMethods()).filter(m -> m.getParameterCount() == 1).map(Method::getName).collect(Collectors.toList());
                    useEntry.generated = internalGenerate(schemaClass, classLoader);
                    String preBuiltFormatString = hasCustom ? null : loggingFormatter.buildFormatString(names);
                    Collection<String> namesSet = hasCustom ? new HashSet<>(names) : Collections.emptySet();
                    useEntry.applicator = (mainMessage, values, t, consumer, requireAllSchemaMethods) -> applyValues(loggingFormatter, names, namesSet, mainMessage, values, t, preBuiltFormatString, consumer, requireAllSchemaMethods);
                }
            }
        }
        return useEntry;
    }

    private void applyValues(LoggingFormatter loggingFormatter, List<String> schemaNames, Collection<String> namesSet, String mainMessage, Map<String, Object> values, Throwable t, String preBuiltFormatMessage, BiConsumer<String, Object[]> consumer, boolean requireAllSchemaMethods) {
        if (requireAllSchemaMethods) {
            if (!values.keySet().containsAll(namesSet)) {
                Set<String> localNamesSet = new HashSet<>(namesSet);
                localNamesSet.removeAll(values.keySet());
                throw new MissingSchemaValueException("Entire schema must be specified. Missing: " + localNamesSet);
            }
        }

        List<String> names;
        String formatString;
        if (preBuiltFormatMessage == null) {
            names = new ArrayList<>(schemaNames);   // add schema names first to preserve ordering
            values.keySet().stream().filter(n -> !namesSet.contains(n)).forEach(names::add);
            formatString = loggingFormatter.buildFormatString(names);
        } else {
            names = schemaNames;
            formatString = preBuiltFormatMessage;
        }

        int argumentQty = names.size() + 1;
        if (t != null) {
            argumentQty += 1;
        }

        Object[] arguments = new Object[argumentQty];
        int argumentIndex = 0;
        for (String name : names) {
            arguments[argumentIndex++] = values.get(name);
        }
        arguments[argumentIndex++] = mainMessage;

        if (t != null) {
            arguments[argumentIndex] = t;
        }
        consumer.accept(formatString, arguments);
    }

    private <T> boolean validateSchemaClass(Class<T> schemaClass) {
        if (!schemaClass.isInterface()) {
            throw new InvalidSchemaException("Schema must be an interface. Schema: " + schemaClass.getName());
        }

        boolean hasCustom = false;
        Set<String> methodNames = new HashSet<>();
        for (Method method : schemaClass.getMethods()) {
            if (!method.getReturnType().isAssignableFrom(schemaClass)) {
                throw new InvalidSchemaException("Schema methods must return " + schemaClass.getSimpleName() + " or a subclass of it. Method: " + method.getName());
            }
            if ((method.getParameterCount() < 1) || (method.getParameterCount() > 2)) {
                throw new InvalidSchemaException("Schema methods must take exactly 1 or 2 arguments. Method: " + method.getName());
            }
            if (method.getParameterCount() == 2) {
                hasCustom = true;
                if (method.getParameterTypes()[0] != String.class) {
                    throw new InvalidSchemaException("For 2 argument schema methods, the first argument must be a string. Method: " + method.getName());
                }
            }
            if (!methodNames.add(method.getName())) {
                throw new InvalidSchemaException("Schema method names must be unique. Duplicate: " + method.getName());
            }
            if (reservedMethodNames.contains(method.getName())) {
                throw new InvalidSchemaException("Schema method name is reserved for internal use. Name: " + method.getName());
            }
        }
        return hasCustom;
    }

    @SuppressWarnings("unchecked")
    private Class internalGenerate(Class schemaClass, ClassLoader classLoader) {
        DynamicType.Builder builder = new ByteBuddy().subclass(Instance.class).implement(schemaClass);
        for (Method method : schemaClass.getMethods()) {
            Implementation methodCall;
            if (method.getParameterCount() == 2) {
                methodCall = invoke(setValueMethod)
                        .withArgument(0)
                        .withArgument(1)
                        .andThen(FixedValue.self());
            } else {
                methodCall = invoke(setValueMethod)
                    .with(method.getName())
                    .withArgument(0)
                    .andThen(FixedValue.self());
            }
            builder = builder.method(named(method.getName())).intercept(methodCall);
        }
        return builder.make().load(classLoader).getLoaded();
    }
}
