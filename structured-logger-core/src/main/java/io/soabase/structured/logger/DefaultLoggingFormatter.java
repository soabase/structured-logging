package io.soabase.structured.logger;

import java.util.Collection;

public class DefaultLoggingFormatter implements LoggingFormatter {
    private final boolean requireAllValues;
    private final boolean mainMessageIsLast;
    private final boolean quoted;

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
    public String buildFormatString(Collection<String> names) {
        StringBuilder format = new StringBuilder();
        if (!mainMessageIsLast) {
            checkPaddingAppend(format, "{}");
        }
        for (String name : names) {
            checkPaddingAppend(format, name);
            if (quoted) {
                format.append("=\"{}\"");
            } else {
                format.append("={}");
            }
        };
        if (mainMessageIsLast) {
            checkPaddingAppend(format, "{}");
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
