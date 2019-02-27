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
package io.soabase.structured.logger;

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LevelLoggers;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import org.slf4j.Logger;

import java.util.function.Consumer;

class StructuredLoggerImpl<T> implements StructuredLogger<T> {
    private final Logger logger;
    private final SchemaMetaInstance<T> schemaMetaInstance;

    StructuredLoggerImpl(Logger logger, SchemaMetaInstance<T> schemaMetaInstance) {
        this.logger = logger;
        this.schemaMetaInstance = schemaMetaInstance;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public void trace(String mainMessage, Throwable t, Consumer<T> statement) {
        consume(LevelLoggers.trace, logger, statement, mainMessage, t);
    }

    @Override
    public void debug(String mainMessage, Throwable t, Consumer<T> statement) {
        consume(LevelLoggers.debug, logger, statement, mainMessage, t);
    }

    @Override
    public void warn(String mainMessage, Throwable t, Consumer<T> statement) {
        consume(LevelLoggers.warn, logger, statement, mainMessage, t);
    }

    @Override
    public void info(String mainMessage, Throwable t, Consumer<T> statement) {
        consume(LevelLoggers.info, logger, statement, mainMessage, t);
    }

    @Override
    public void error(String mainMessage, Throwable t, Consumer<T> statement) {
        consume(LevelLoggers.error, logger, statement, mainMessage, t);
    }

    @Override
    public <S> StructuredLogger<S> as(Class<S> schema) {
        return StructuredLoggerFactory.getLogger(logger, schema, schemaMetaInstance.loggingFormatter());
    }

    @Override
    public <S> StructuredLogger<S> as(Class<S> schema, LoggingFormatter formatter) {
        return StructuredLoggerFactory.getLogger(logger, schema, formatter);
    }

    private void consume(LevelLogger levelLogger, Logger logger, Consumer<T> statement, String mainMessage, Throwable t) {
        if (levelLogger.isEnabled(logger)) {
            T instance = schemaMetaInstance.newSchemaInstance();
            statement.accept(instance);
            schemaMetaInstance.apply(levelLogger, logger, instance, mainMessage, t);
        }
    }
}
