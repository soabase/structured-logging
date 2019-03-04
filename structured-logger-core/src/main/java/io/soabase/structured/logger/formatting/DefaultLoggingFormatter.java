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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static io.soabase.structured.logger.formatting.DefaultLoggingFormatter.Option.*;

public class DefaultLoggingFormatter implements LoggingFormatter {
    private final boolean mainMessageIsLast;
    private final boolean quoteValues;
    private final boolean escapeValues;
    private final boolean snakeCase;
    private final int stringBuilderCapacity;

    private static final int DEFAULT_CAPACITY = 128;

    public enum Option {
        MAIN_MESSAGE_IS_LAST,
        QUOTE_VALUES,
        ESCAPE_VALUES,
        SNAKE_CASE
    }

    public DefaultLoggingFormatter(Option... options) {
        this(DEFAULT_CAPACITY, options);
    }

    public DefaultLoggingFormatter(int stringBuilderCapacity, Option... options) {
        Collection<Option> optionsSet = new HashSet<>(Arrays.asList(options));

        this.mainMessageIsLast = optionsSet.contains(MAIN_MESSAGE_IS_LAST);
        this.quoteValues = optionsSet.contains(QUOTE_VALUES);
        this.escapeValues = optionsSet.contains(ESCAPE_VALUES);
        this.snakeCase = optionsSet.contains(SNAKE_CASE);
        this.stringBuilderCapacity = stringBuilderCapacity;
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, List<String> schemaNames, Arguments arguments, String mainMessage, Throwable t) {
        StringBuilder logMessage = new StringBuilder(stringBuilderCapacity);
        boolean needsSpace = false;
        boolean hasMainMessage = !mainMessage.isEmpty();
        if (hasMainMessage) {
            if (!mainMessageIsLast) {
                needsSpace = true;
                logMessage.append(mainMessage);
            }
        }

        for (int i = 0; i < arguments.size(); ++i, needsSpace = true) {
            Object value = arguments.get(i);
            if (needsSpace) {
                logMessage.append(" ");
            }
            String name = filterSchemaName(schemaNames.get(i));
            logMessage.append(name).append("=");
            if (quoteValues) {
                logMessage.append("\"");
            }
            if (escapeValues) {
                addEscapedValue(logMessage, value);
            } else {
                logMessage.append(value);
            }
            if (quoteValues) {
                logMessage.append("\"");
            }
        }

        if (hasMainMessage && mainMessageIsLast) {
            if (arguments.size() > 0) {
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

    private String filterSchemaName(String name) {
        return snakeCase ? toSnakeCase(name) : name;
    }

    public static String toSnakeCase(String name) {
        StringBuilder str = new StringBuilder(name.length() * 2);
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (str.length() > 0) {
                    str.append('_');
                }
                str.append(Character.toLowerCase(c));
            } else
            {
                str.append(c);
            }
        }
        name = str.toString();
        return name;
    }

    private void addEscapedValue(StringBuilder logMessage, Object value) {
        String str = String.valueOf(value);
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '"': {
                    logMessage.append('\\').append(c);
                    break;
                }

                case '\r': {
                    logMessage.append("\\r");
                    break;
                }

                case '\n': {
                    logMessage.append("\\n");
                    break;
                }

                case ' ':
                case '\t': {
                    if (!quoteValues) {
                        logMessage.append('\\');
                    }
                    logMessage.append(c);
                    break;
                }

                default: {
                    logMessage.append(c);
                    break;
                }
            }
        }
    }
}
