package io.soabase.structured.logger.util;

import io.soabase.structured.logger.formatting.LevelLogger;

import java.util.List;

public class LoggingEntry {
    public final LevelLogger levelLogger;
    public final List<String> schemaNames;
    public final Object[] arguments;
    public final String mainMessage;
    public final Throwable t;

    public LoggingEntry(LevelLogger levelLogger, List<String> schemaNames, Object[] arguments, String mainMessage, Throwable t) {
        this.levelLogger = levelLogger;
        this.schemaNames = schemaNames;
        this.arguments = arguments;
        this.mainMessage = mainMessage;
        this.t = t;
    }
}
