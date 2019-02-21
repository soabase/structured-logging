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

import io.soabase.structured.logger.generation.Generated;
import io.soabase.structured.logger.generation.Instance;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class StructuredLoggerImpl<T> implements StructuredLogger<T> {
    private final LoggerFacade logger;
    private final Generated<T> generated;

    StructuredLoggerImpl(LoggerFacade logger, Generated<T> generated) {
        this.logger = logger;
        this.generated = generated;
    }

    @Override
    public LoggerFacade logger() {
        return logger;
    }

    @Override
    public void trace(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isTraceEnabled()) {
            consume(statement, mainMessage, t, logger::trace);
        }
    }

    @Override
    public void debug(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isDebugEnabled()) {
            consume(statement, mainMessage, t, logger::debug);
        }
    }

    @Override
    public void warn(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isWarnEnabled()) {
            consume(statement, mainMessage, t, logger::warn);
        }
    }

    @Override
    public void info(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isInfoEnabled()) {
            consume(statement, mainMessage, t, logger::info);
        }
    }

    @Override
    public void error(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isErrorEnabled()) {
            consume(statement, mainMessage, t, logger::error);
        }
    }

    private void consume(Consumer<T> statement, String mainMessage, Throwable t, BiConsumer<String, Object[]> logger) {
        T instance = generated.newInstance(t != null);
        statement.accept(instance);
        generated.apply(instance, mainMessage, t, logger);
    }
}
