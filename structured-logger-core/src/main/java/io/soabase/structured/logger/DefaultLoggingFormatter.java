package io.soabase.structured.logger;

import java.util.Collection;

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
    public String buildFormatString(Collection<String> names) {
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
