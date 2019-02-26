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

import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.util.function.Consumer;

/**
 * A Logger instance. Roughly corresponds to {@link Logger} however each logging
 * method accepts a schema consumer
 */
public interface StructuredLogger<T> {
    /**
     * Returns the underlying logger
     *
     * @return logger
     */
    Logger logger();

    default void trace(Consumer<T> statement) {
        trace("", null, statement);
    }

    default void debug(Consumer<T> statement) {
        debug("", null, statement);
    }

    default void warn(Consumer<T> statement) {
        warn("", null, statement);
    }

    default void info(Consumer<T> statement) {
        info("", null, statement);
    }

    default void error(Consumer<T> statement) {
        error("", null, statement);
    }

    default void trace(String mainMessage, Consumer<T> statement) {
        trace(mainMessage, null, statement);
    }

    default void debug(String mainMessage, Consumer<T> statement) {
        debug(mainMessage, null, statement);
    }

    default void warn(String mainMessage, Consumer<T> statement) {
        warn(mainMessage, null, statement);
    }

    default void info(String mainMessage, Consumer<T> statement) {
        info(mainMessage, null, statement);
    }

    default void error(String mainMessage, Consumer<T> statement) {
        error(mainMessage, null, statement);
    }

    void trace(String mainMessage, Throwable t, Consumer<T> statement);

    void debug(String mainMessage, Throwable t, Consumer<T> statement);

    void warn(String mainMessage, Throwable t, Consumer<T> statement);

    void info(String mainMessage, Throwable t, Consumer<T> statement);

    void error(String mainMessage, Throwable t, Consumer<T> statement);

    /**
     * Utility - returns the underlying logger and formatter with a different schema
     *
     * @param schema new schema
     * @return logger
     */
    <S> StructuredLogger<S> as(Class<S> schema);

    /**
     * Utility - returns the underlying logger with a different schema and formatter
     *
     * @param schema new schema
     * @param formatter formatter
     * @return logger
     */
    <S> StructuredLogger<S> as(Class<S> schema, LoggingFormatter formatter);
}
