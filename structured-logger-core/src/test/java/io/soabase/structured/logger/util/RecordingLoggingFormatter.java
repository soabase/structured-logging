package io.soabase.structured.logger.util;

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RecordingLoggingFormatter implements LoggingFormatter {
    public final List<LoggingEntry> entries = new ArrayList<>();

    @Override
    public int indexForArgument(String schemaMethodName, int ordinalIndex) {
        return ordinalIndex;
    }

    @Override
    public int argumentQty(int schemaQty, boolean hasException) {
        return schemaQty;
    }

    @Override
    public boolean requireAllValues() {
        return false;
    }

    @Override
    public String buildFormatString(List<String> schemaNames) {
        return "";
    }

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, String formatString, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t) {
        entries.add(new LoggingEntry(levelLogger, schemaNames, arguments, mainMessage, t));
    }
}
