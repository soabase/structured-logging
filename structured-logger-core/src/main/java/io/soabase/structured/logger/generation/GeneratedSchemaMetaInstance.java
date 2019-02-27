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

import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.spi.SchemaMetaInstanceAdaptor;
import io.soabase.structured.logger.spi.SchemaNames;

class GeneratedSchemaMetaInstance<T> extends SchemaMetaInstanceAdaptor<T> {
    GeneratedSchemaMetaInstance(Class<T> generatedClass, InstanceFactory<T> instanceFactory, SchemaNames schemaNames, LoggingFormatter loggingFormatter) {
        super(() -> newInstance(generatedClass, instanceFactory, schemaNames), GeneratedSchemaMetaInstance::asArguments, schemaNames, loggingFormatter);
    }

    private static <T> T newInstance(Class<T> generatedClass, InstanceFactory<T> instanceFactory, SchemaNames schemaNames) {
        try {
            T instance = instanceFactory.newInstance();
            ((Instance)instance).arguments = new Object[schemaNames.getNames().size()];
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Could not allocate schema instance: " + generatedClass.getName(), e);
        }
    }

    private static <T> Arguments asArguments(T instance) {
        Object[] arguments = ((Instance)instance).arguments;
        return new Arguments() {
            @Override
            public int size() {
                return arguments.length;
            }

            @Override
            public Object get(int index) {
                return arguments[index];
            }
        };
    }
}
