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
package io.soabase.structured.logger.util;

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RecordingLoggingFormatter implements LoggingFormatter {
    public final List<LoggingEntry> entries = new ArrayList<>();

    @Override
    public int indexForArgument(String schemaMethodName, int ordinalIndex) {
        return ordinalIndex;
    }

    @Override
    public int argumentQty(int schemaQty, boolean hasException) {
        return schemaQty;
    }

    @Override
    public boolean requireAllValues() {
        return false;
    }

    @Override
    public String buildFormatString(List<String> schemaNames) {
        return "";
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, String formatString, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t) {
        entries.add(new LoggingEntry(levelLogger, schemaNames, arguments, mainMessage, t));
    }
}
