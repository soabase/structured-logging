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

import java.util.Collection;

@FunctionalInterface
public interface LoggingFormatter {
    String buildFormatString(Collection<String> names);

    default boolean requireAllValues() {
        return false;
    }

    default boolean mainMessageIsLast() {
        return true;
    }

    LoggingFormatter defaultLoggingFormatter = new DefaultLoggingFormatter(false, true, false);

    static LoggingFormatter requireAllValues(LoggingFormatter formatter) {
        return new LoggingFormatter() {
            @Override
            public String buildFormatString(Collection<String> names) {
                return formatter.buildFormatString(names);
            }

            @Override
            public boolean requireAllValues() {
                return true;
            }

            @Override
            public boolean mainMessageIsLast() {
                return formatter.mainMessageIsLast();
            }
        };
    }

    static LoggingFormatter mainMessageIsFirst(LoggingFormatter formatter) {
        return new LoggingFormatter() {
            @Override
            public String buildFormatString(Collection<String> names) {
                return formatter.buildFormatString(names);
            }

            @Override
            public boolean requireAllValues() {
                return formatter.requireAllValues();
            }

            @Override
            public boolean mainMessageIsLast() {
                return false;
            }
        };
    }
}
