package io.soabase.structured.logger;

import java.util.Collection;

@FunctionalInterface
public interface LoggingFormatter {
    String buildFormatString(Collection<String> names);

    default boolean requireAllValues() {
        return false;
    }

    LoggingFormatter defaultLoggingFormatter = names -> {
        StringBuilder format = new StringBuilder();
        names.forEach(name -> {
            if (format.length() > 0) {
                format.append(' ');
            }
            format.append(name).append("={}");
        });
        if (format.length() > 0) {
            format.append(' ');
        }
        format.append("{}");
        return format.toString();
    };

    static LoggingFormatter requiringAllValues(LoggingFormatter formatter) {
        return new LoggingFormatter() {
            @Override
            public String buildFormatString(Collection<String> names) {
                return formatter.buildFormatString(names);
            }

            @Override
            public boolean requireAllValues() {
                return true;
            }
        };
    }
}
