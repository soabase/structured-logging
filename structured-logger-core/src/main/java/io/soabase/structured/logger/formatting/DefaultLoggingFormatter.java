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

import io.soabase.structured.logger.LoggerFacade;
import io.soabase.structured.logger.LoggerLevel;

import java.util.List;

public class DefaultLoggingFormatter implements LoggingFormatter {
    private final boolean requireAllValues;
    private final boolean mainMessageIsLast;
    private final boolean quoted;

    private static final String REPLACEMENT_STR = "{}";
    private static final String QUOTED_REPLACEMENT_EQUALS_STR = "=\"{}\"";
    private static final String REPLACEMENT_EQUALS_STR = "={}";

    public DefaultLoggingFormatter(boolean requireAllValues, boolean mainMessageIsLast, boolean quoted) {
        this.requireAllValues = requireAllValues;
        this.mainMessageIsLast = mainMessageIsLast;
        this.quoted = quoted;
    }

    @Override
    public boolean requireAllValues() {
        return requireAllValues;
    }

    @Override
    public int indexForArgument(String schemaMethodName, int ordinalIndex) {
        return mainMessageIsLast ? ordinalIndex : (ordinalIndex + 1);
    }

    @Override
    public int argumentQty(int schemaQty, boolean hasException) {
        return hasException ? (schemaQty + 2) : (schemaQty + 1);
    }

    @Override
    public void apply(LoggerLevel level, LoggerFacade logger, String formatString, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t) {
        if (t != null) {
            arguments[mainMessageIsLast ? (arguments.length - 2) : 0] = mainMessage;
            arguments[arguments.length - 1] = t;
        } else {
            arguments[mainMessageIsLast ? (arguments.length - 1) : 0] = mainMessage;
        }
        level.log(logger, formatString, arguments);
    }

    @Override
    public String buildFormatString(List<String> names) {
        StringBuilder format = new StringBuilder();
        if (!mainMessageIsLast) {
            checkPaddingAppend(format, REPLACEMENT_STR);
        }
        for (String name : names) {
            checkPaddingAppend(format, name);
            if (quoted) {
                format.append(QUOTED_REPLACEMENT_EQUALS_STR);
            } else {
                format.append(REPLACEMENT_EQUALS_STR);
            }
        };
        if (mainMessageIsLast) {
            checkPaddingAppend(format, REPLACEMENT_STR);
        }
        return format.toString();
    }

    private void checkPaddingAppend(StringBuilder format, String str) {
        if (format.length() > 0) {
            format.append(' ');
        }
        format.append(str);
    }
}
