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
