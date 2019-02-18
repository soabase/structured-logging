package io.soabase.structured.logger;

import java.util.Collection;

@FunctionalInterface
public interface LoggingFormatter {
    String buildFormatString(Collection<String> names);

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
}
