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

import io.soabase.structured.logger.LoggerFacade;
import io.soabase.structured.logger.LoggerLevel;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.formatting.LoggingFormatter;

import java.lang.reflect.Constructor;
import java.util.List;

class GeneratedImpl<T> implements Generated<T> {
    private final Class<T> generatedClass;
    private final Constructor<T> declaredConstructor;
    private final List<String> schemaNames;
    private final LoggingFormatter loggingFormatter;
    private final String formatString;

    GeneratedImpl(Class<T> generatedClass, List<String> schemaNames, LoggingFormatter loggingFormatter) {
        try {
            this.generatedClass = generatedClass;
            this.schemaNames = schemaNames;
            this.loggingFormatter = loggingFormatter;
            this.formatString = loggingFormatter.buildFormatString(schemaNames);
            this.declaredConstructor = this.generatedClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not cache schema meta data: " + generatedClass.getName(), e);
        }
    }

    @Override
    public LoggingFormatter loggingFormatter() {
        return loggingFormatter;
    }

    @Override
    public T newInstance(boolean hasException) {
        try {
            T instance = declaredConstructor.newInstance();
            ((Instance)instance).arguments = new Object[loggingFormatter.argumentQty(schemaNames.size(), hasException)];
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Could not allocate schema instance: " + generatedClass.getName(), e);
        }
    }

    @Override
    public void apply(LoggerLevel level, LoggerFacade logger, T instance, String mainMessage, Throwable t) {
        Object[] arguments = ((Instance)instance).arguments;

        if (loggingFormatter.requireAllValues()) {
            int index = 0;
            for (Object argument : arguments) {
                if (argument == null) {
                    throw new MissingSchemaValueException("Entire schema must be specified. Missing: " + schemaNames.get(index));
                }
                ++index;
            }
        }

        loggingFormatter.apply(level, logger, formatString, schemaNames, arguments, mainMessage, t);
    }
}
