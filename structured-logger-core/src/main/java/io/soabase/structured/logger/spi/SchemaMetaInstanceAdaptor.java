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
package io.soabase.structured.logger.spi;

import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Enforces general behavior of a SchemaMetaInstance
 */
public abstract class SchemaMetaInstanceAdaptor<T> implements SchemaMetaInstance<T> {
    private final Supplier<T> instanceFactory;
    private final Function<T, Arguments> argumentsAccessor;
    private final SchemaNames schemaNames;
    private final LoggingFormatter loggingFormatter;

    protected SchemaMetaInstanceAdaptor(Supplier<T> instanceFactory, Function<T, Arguments> argumentsAccessor, SchemaNames schemaNames, LoggingFormatter loggingFormatter) {
        this.instanceFactory = instanceFactory;
        this.argumentsAccessor = argumentsAccessor;
        this.schemaNames = schemaNames;
        this.loggingFormatter = loggingFormatter;
    }

    @Override
    public T newSchemaInstance() {
        return instanceFactory.get();
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, T instance, String mainMessage, Throwable t) {
        Arguments arguments = argumentsAccessor.apply(instance);
        schemaNames.validateRequired(arguments);
        loggingFormatter.apply(levelLogger, logger, schemaNames.getFormattedNames(), arguments, mainMessage, t);
    }
}
