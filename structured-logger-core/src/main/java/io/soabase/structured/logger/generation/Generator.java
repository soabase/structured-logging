package io.soabase.structured.logger.generation;

import io.soabase.structured.logger.LoggingFormatter;
import io.soabase.structured.logger.exception.InvalidSchemaException;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.schemas.WithFormat;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private static final Method setValueAtIndexMethod;
    private static final Method formattedAtIndexMethod;
    static {
        try {
            setValueAtIndexMethod = Instance.class.getMethod("_InternalSetValueAtIndex", Integer.TYPE, Object.class);
            formattedAtIndexMethod = Instance.class.getMethod("_InternalFormattedAtIndex", Integer.TYPE, String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find internal method", e);
        }
    }

    public void clearCache() {
        generated.clear();
    }

    private static class Entry<T> implements Generated<T> {
        private final Class<T> generated;
        private final int schemaQty;
        private final String formatString;
        private final boolean requireAllValues;
        private final int mainMessageIndex;
        private final int exceptionIndex;

        Entry(Class<T> generated, int schemaQty, String formatString, boolean requireAllValues, boolean mainMessageIsLast) {
            this.generated = generated;
            this.schemaQty = schemaQty;
            this.formatString = formatString;
            this.requireAllValues = requireAllValues;
            this.mainMessageIndex = mainMessageIsLast ? schemaQty : 0;
            this.exceptionIndex = schemaQty + 1;
        }

        @Override
        public T newInstance(boolean hasException) {
            try {
                T instance = generated.getDeclaredConstructor().newInstance();
                ((Instance)instance).arguments = new Object[hasException ? (schemaQty + 2) : (schemaQty + 1)];
                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Could not allocate schema instance: " + generated.getName(), e);
            }
        }

        @Override
        public void apply(T instance, String mainMessage, Throwable t, BiConsumer<String, Object[]> consumer) {
            Object[] arguments = ((Instance)instance).arguments;
            arguments[mainMessageIndex] = mainMessage;
            if (t != null) {
                arguments[exceptionIndex] = t;
            }

            if (requireAllValues) {
                for (Object argument : arguments) {
                    if (argument == null) {
                        throw new MissingSchemaValueException("Entire schema must be specified. Missing: " + "foo");    // TODO
                    }
                }
            }

            consumer.accept(formatString, arguments);
        }
    }

    @SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
    public <T> Generated<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter) {
        return generated.computeIfAbsent(schemaClass, __ -> {
            List<String> schemaNames = validateSchemaClass(schemaClass);
            Class generatedClass = internalGenerate(schemaClass, classLoader, loggingFormatter.mainMessageIsLast());
            String formatString = loggingFormatter.buildFormatString(schemaNames);
            return new Entry(generatedClass, schemaNames.size(), formatString, loggingFormatter.requireAllValues(), loggingFormatter.mainMessageIsLast());
        });
    }

    private <T> List<String> validateSchemaClass(Class<T> schemaClass) {
        if (!schemaClass.isInterface()) {
            throw new InvalidSchemaException("Schema must be an interface. Schema: " + schemaClass.getName());
        }

        List<String> schemaNames = new ArrayList<>();
        Set<String> usedMethodNames = new HashSet<>();
        for (Method method : schemaClass.getMethods()) {
            if (!method.getReturnType().isAssignableFrom(schemaClass)) {
                throw new InvalidSchemaException("Schema methods must return " + schemaClass.getSimpleName() + " or a subclass of it. Method: " + method.getName());
            }
            if (!usedMethodNames.add(method.getName())) {
                throw new InvalidSchemaException("Schema method names must be unique. Duplicate: " + method.getName());
            }
            if (!method.getDeclaringClass().equals(WithFormat.class)) {
                if (method.getParameterCount() != 1) {
                    throw new InvalidSchemaException("Schema methods must take exactly 1 argument. Method: " + method.getName());
                }
                if (reservedMethodNames.contains(method.getName())) {
                    throw new InvalidSchemaException("Schema method name is reserved for internal use. Name: " + method.getName());
                }
            }
            schemaNames.add(method.getName());
        }
        return Collections.unmodifiableList(schemaNames);
    }

    @SuppressWarnings("unchecked")
    private Class internalGenerate(Class schemaClass, ClassLoader classLoader, boolean mainMessageIsLast) {
        DynamicType.Builder builder = new ByteBuddy().subclass(Instance.class).implement(schemaClass);
        int schemaIndex = mainMessageIsLast ? 0 : 1;
        for (Method method : schemaClass.getMethods()) {
            Implementation methodCall;
            if (method.getDeclaringClass().equals(WithFormat.class)) {
                methodCall = invoke(formattedAtIndexMethod)
                        .with(schemaIndex++)
                        .withArgument(0)
                        .withArgument(1)
                        .andThen(FixedValue.self());
            } else {
                methodCall = invoke(setValueAtIndexMethod)
                        .with(schemaIndex++)
                        .withArgument(0)
                        .andThen(FixedValue.self());
            }
            builder = builder.method(named(method.getName())).intercept(methodCall);
        }
        return builder.make().load(classLoader).getLoaded();
    }
}
