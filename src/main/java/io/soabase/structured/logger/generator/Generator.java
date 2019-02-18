package io.soabase.structured.logger.generator;

import io.soabase.structured.logger.Instance;
import io.soabase.structured.logger.InvalidSchemaException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    private static class Entry {
        volatile Class generated;
    }

    public void clearCache() {
        generated.clear();
    }

    @SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
    public <T> Class<T> generate(Class<T> schemaClass, ClassLoader classLoader) {
        Entry newEntry = new Entry();
        Entry existingEntry = generated.putIfAbsent(schemaClass, newEntry);
        final Entry useEntry = (existingEntry != null) ? existingEntry : newEntry;
        if (useEntry.generated == null) {
            validateSchemaClass(schemaClass);
            synchronized (useEntry) {
                if (useEntry.generated == null) {
                    useEntry.generated = internalGenerate(schemaClass, classLoader);
                }
            }
        }
        return useEntry.generated;
    }

    private <T> void validateSchemaClass(Class<T> schemaClass) {
        if (!schemaClass.isInterface()) {
            throw new InvalidSchemaException("Schema must be an interface. Schema: " + schemaClass.getName());
        }

        Set<String> methodNames = new HashSet<>();
        for (Method method : schemaClass.getDeclaredMethods()) {
            if (!method.getReturnType().equals(schemaClass)) {
                throw new InvalidSchemaException("Schema methods must return " + schemaClass.getSimpleName() + ". Method: " + method.getName());
            }
            if ((method.getParameterCount() < 1) || (method.getParameterCount() > 2)) {
                throw new InvalidSchemaException("Schema methods must take exactly 1 or 2 arguments. Method: " + method.getName());
            }
            if (method.getParameterCount() == 2) {
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
    }

    @SuppressWarnings("unchecked")
    private Class internalGenerate(Class schemaClass, ClassLoader classLoader) {
        DynamicType.Builder builder = new ByteBuddy().subclass(Instance.class).implement(schemaClass);
        for (Method method : schemaClass.getDeclaredMethods()) {
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
