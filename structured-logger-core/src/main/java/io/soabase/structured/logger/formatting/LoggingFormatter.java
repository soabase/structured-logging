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
package io.soabase.structured.logger.formatting;

import org.slf4j.Logger;

import java.util.List;

import static io.soabase.structured.logger.formatting.DefaultLoggingFormatter.Option.ESCAPE_VALUES;
import static io.soabase.structured.logger.formatting.DefaultLoggingFormatter.Option.MAIN_MESSAGE_IS_LAST;
import static io.soabase.structured.logger.formatting.DefaultLoggingFormatter.Option.QUOTE_VALUES;

@FunctionalInterface
public interface LoggingFormatter {
    void apply(LevelLogger levelLogger, Logger logger, List<String> schemaNames, Arguments arguments, String mainMessage, Throwable t);

    default String formatSchemaName(String name) {
        return name;
    }

    LoggingFormatter defaultLoggingFormatter = new DefaultLoggingFormatter(MAIN_MESSAGE_IS_LAST, ESCAPE_VALUES, QUOTE_VALUES);
}
