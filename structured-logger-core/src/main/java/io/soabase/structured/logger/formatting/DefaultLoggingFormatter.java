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

public class DefaultLoggingFormatter implements LoggingFormatter {
    private final boolean mainMessageIsLast;
    private final boolean quoted;
    private final int stringBuilderCapacity;

    private static final int DEFAULT_CAPACITY = 128;

    public DefaultLoggingFormatter(boolean mainMessageIsLast, boolean quoted) {
        this(mainMessageIsLast, quoted, DEFAULT_CAPACITY);
    }

    public DefaultLoggingFormatter(boolean mainMessageIsLast, boolean quoted, int stringBuilderCapacity) {
        this.mainMessageIsLast = mainMessageIsLast;
        this.quoted = quoted;
        this.stringBuilderCapacity = stringBuilderCapacity;
    }

    @Override
    public int indexForArgument(String schemaMethodName, int ordinalIndex) {
        return ordinalIndex;
    }

    @Override
    public int argumentQty(int schemaQty, boolean hasException) {
        return schemaQty;
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, String formatString, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t) {
        StringBuilder logMessage = new StringBuilder(stringBuilderCapacity);
        if (!mainMessageIsLast) {
            logMessage.append(mainMessage);
        }

        for (int i = 0; i < arguments.length; ++i) {
            if (!mainMessageIsLast || (i > 0)) {
                logMessage.append(" ");
            }
            logMessage.append(schemaNames.get(i)).append("=");
            if (quoted) {
                logMessage.append("\"").append(arguments[i]).append("\"");
            } else {
                logMessage.append(arguments[i]);
            }
        }

        if (mainMessageIsLast) {
            if (arguments.length > 0) {
                logMessage.append(" ");
            }
            logMessage.append(mainMessage);
        }

        if (t != null) {
            levelLogger.log(logger, logMessage.toString(), t);
        } else {
            levelLogger.log(logger, logMessage.toString());
        }
    }

    @Override
    public String buildFormatString(List<String> names) {
        return "";
    }
}
