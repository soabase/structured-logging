/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.structured.logger.generation;

import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.schemas.WithFormat;
import io.soabase.structured.logger.spi.SchemaFactory;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import io.soabase.structured.logger.spi.SchemaNames;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bytebuddy.implementation.MethodCall.invoke;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class Generator implements SchemaFactory {
    private final Map<Key, SchemaMetaInstance> generated = new ConcurrentHashMap<>();
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

    @Override
    public void clearCache() {
        generated.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> SchemaMetaInstance<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter) {
        return generated.computeIfAbsent(new Key(schemaClass, loggingFormatter), __ -> {
            SchemaNames schemaNames = SchemaNames.build(schemaClass, reservedMethodNames);
            ByteBuddy byteBuddy = new ByteBuddy();
            Class generatedClass = internalGenerate(byteBuddy, schemaClass, classLoader, schemaNames.getNames());
            InstanceFactory<T> instanceFactory = generateInstanceFactory(byteBuddy, generatedClass);
            return new GeneratedSchemaMetaInstance<>(generatedClass, instanceFactory, schemaNames, loggingFormatter);
        });
    }

    private Class internalGenerate(ByteBuddy byteBuddy, Class schemaClass, ClassLoader classLoader, List<String> names) {
        DynamicType.Builder builder = byteBuddy.subclass(Instance.class).implement(schemaClass);
        for (Method method : schemaClass.getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }
            int thisIndex = names.indexOf(method.getName());
            Implementation methodCall;
            if (method.getDeclaringClass().equals(WithFormat.class)) {
                methodCall = invoke(formattedAtIndexMethod)
                        .with(thisIndex)
                        .withArgument(0)
                        .withArgument(1)
                        .andThen(FixedValue.self());
            } else {
                methodCall = invoke(setValueAtIndexMethod)
                        .with(thisIndex)
                        .withArgument(0)
                        .andThen(FixedValue.self());
            }
            builder = builder.method(named(method.getName())).intercept(methodCall);
        }
        return builder.make().load(classLoader).getLoaded();
    }

    @SuppressWarnings("unchecked")
    private <T> InstanceFactory generateInstanceFactory(ByteBuddy byteBuddy, Class<T> clazz) {
        try {
            DynamicType.Builder<InstanceFactory> builder = byteBuddy
                    .subclass(InstanceFactory.class)
                    .method(named("newInstance")).intercept(MethodDelegation.toConstructor(clazz));
            return builder.make().load(clazz.getClassLoader()).getLoaded().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't not create InstanceFactory for: " + clazz.getName(), e);
        }
    }
}
