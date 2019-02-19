package io.soabase.structured.logger.util;

import io.soabase.structured.logger.LoggerFacade;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestLoggerFacade implements LoggerFacade {
    public static class Entry {
        public final String type;
        public final String message;
        public final Object[] arguments;

        public Entry(String type, String message, Object[] arguments) {
            this.type = type;
            this.message = message;
            this.arguments = Arrays.copyOf(arguments, arguments.length);
        }
    }

    public final List<Entry> entries = new CopyOnWriteArrayList<>();

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void trace(String message, Object[] arguments) {
        entries.add(new Entry("trace", message, arguments));
    }

    @Override
    public void debug(String message, Object[] arguments) {
        entries.add(new Entry("debug", message, arguments));
    }

    @Override
    public void warn(String message, Object[] arguments) {
        entries.add(new Entry("warn", message, arguments));
    }

    @Override
    public void info(String message, Object[] arguments) {
        entries.add(new Entry("info", message, arguments));
    }

    @Override
    public void error(String message, Object[] arguments) {
        entries.add(new Entry("error", message, arguments));
    }
}
