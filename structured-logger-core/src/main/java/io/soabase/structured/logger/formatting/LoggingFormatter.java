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

import io.soabase.structured.logger.LoggerLevel;
import org.slf4j.Logger;

import java.util.List;

public interface LoggingFormatter {
    int indexForArgument(String schemaMethodName, int ordinalIndex);

    int argumentQty(int schemaQty, boolean hasException);

    boolean requireAllValues();     // TODO this seems out of place here - where to put it?

    String buildFormatString(List<String> schemaNames);

    void apply(LoggerLevel level, Logger logger, String formatString, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t);

    LoggingFormatter defaultLoggingFormatter = new DefaultLoggingFormatter(false, true, true);
}
