package io.soabase.structured.logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface LoggingFormatter {
    String buildFormatString(Collection<String> names);

    default boolean requireAllValues() {
        return false;
    }

    default Object[] buildArguments(List<String> names, String mainMessage, Throwable t, Map<String, Object> values) {
        boolean hasException = (t != null);
        int argumentQty = hasException ? (names.size() + 2) : (names.size() + 1);

        Object[] arguments = new Object[argumentQty];
        int argumentIndex = 0;
        for (String name : names) {
            arguments[argumentIndex++] = values.get(name);
        }
        arguments[argumentIndex++] = mainMessage;

        if (hasException) {
            arguments[argumentIndex] = t;
        }

        return arguments;
    }

    LoggingFormatter defaultLoggingFormatter = names -> formatStringFieldValue(names, false);

    LoggingFormatter quotedLoggingFormatter = names -> formatStringFieldValue(names, true);

    static String formatStringFieldValue(Collection<String> names, boolean quoted) {
        StringBuilder format = new StringBuilder();
        names.forEach(name -> {
            if (format.length() > 0) {
                format.append(' ');
            }
            if (quoted) {
                format.append(name).append("=\"{}\"");
            } else {
                format.append(name).append("={}");
            }
        });
        if (format.length() > 0) {
            format.append(' ');
        }
        format.append("{}");
        return format.toString();
    }

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
