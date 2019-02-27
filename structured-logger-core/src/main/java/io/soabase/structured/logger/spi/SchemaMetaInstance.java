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

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

public interface SchemaMetaInstance<T> {
    /**
     * Return a new instance of the schema for use in a log statement
     *
     * @return instance
     */
    T newSchemaInstance();

    /**
     * Return the logging formatter registered or the one to use
     *
     * @return formatter
     */
    LoggingFormatter loggingFormatter();

    /**
     * Apply the now processed schema instance to the logger - i.e. write the log message
     *
     * @param levelLogger the log level
     * @param logger the SLF4J logger
     * @param instance schema instance
     * @param mainMessage main message or an empty string
     * @param t the exception to log or null
     */
    void apply(LevelLogger levelLogger, Logger logger, T instance, String mainMessage, Throwable t);
}
