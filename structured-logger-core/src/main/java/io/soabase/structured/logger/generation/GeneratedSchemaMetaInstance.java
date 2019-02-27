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

import io.soabase.structured.logger.StructuredLoggerFactory;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import org.slf4j.Logger;

class GeneratedSchemaMetaInstance<T> implements SchemaMetaInstance<T> {
    private final Class<T> generatedClass;
    private final SchemaNames schemaNames;
    private final LoggingFormatter loggingFormatter;
    private final InstanceFactory<T> instanceFactory;

    GeneratedSchemaMetaInstance(Class<T> generatedClass, InstanceFactory<T> instanceFactory, SchemaNames schemaNames, LoggingFormatter loggingFormatter) {
        this.instanceFactory = instanceFactory;
        this.generatedClass = generatedClass;
        this.schemaNames = schemaNames;
        this.loggingFormatter = loggingFormatter;
    }

    @Override
    public LoggingFormatter loggingFormatter() {
        return loggingFormatter;
    }

    @Override
    public T newSchemaInstance() {
        try {
            T instance = instanceFactory.newInstance();
            ((Instance)instance).arguments = new Object[schemaNames.names.size()];
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Could not allocate schema instance: " + generatedClass.getName(), e);
        }
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, T instance, String mainMessage, Throwable t) {
        Object[] arguments = ((Instance)instance).arguments;

        if (StructuredLoggerFactory.requiredValuesEnabled() && !schemaNames.requireds.isEmpty()) {
            schemaNames.requireds.forEach(index -> {
                if (arguments[index] == null) {
                    throw new MissingSchemaValueException("Entire schema must be specified. Missing: " + schemaNames.names.get(index));
                }
            });
        }

        Arguments argumentsWrapper = new Arguments() {
            @Override
            public int size() {
                return arguments.length;
            }

            @Override
            public Object get(int index) {
                return arguments[index];
            }
        };
        loggingFormatter.apply(levelLogger, logger, schemaNames.names, argumentsWrapper, mainMessage, t);
    }
}
