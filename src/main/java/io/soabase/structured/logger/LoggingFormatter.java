package io.soabase.structured.logger;

import java.util.Map;

@FunctionalInterface
public interface LoggingFormatter {
    String format(String mainMessage, Map<String, Object> values);

    LoggingFormatter defaultLoggingFormatter = (mainMessage, values) -> {
        StringBuilder format = new StringBuilder(mainMessage);
        values.forEach((name, value) -> {
            if (format.length() > 0) {
                format.append(' ');
            }
            format.append(name).append('=').append(value);
        });
        return format.toString();
    };

    LoggingFormatter messageAtEndLoggingFormatter = (mainMessage, values) -> {
        StringBuilder format = new StringBuilder();
        values.forEach((name, value) -> {
            if (format.length() > 0) {
                format.append(' ');
            }
            format.append(name).append('=').append(value);
        });
        if (!mainMessage.isEmpty()) {
            if (format.length() > 0) {
                format.append(' ');
            }
            format.append(mainMessage);
        }
        return format.toString();
    };
}
