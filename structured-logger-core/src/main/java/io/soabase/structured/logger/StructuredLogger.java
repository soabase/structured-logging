package io.soabase.structured.logger;

import org.slf4j.Logger;

import java.util.function.Consumer;

public interface StructuredLogger<T> {
    Logger logger();

    default void trace(Consumer<T> statement) {
        trace("", null, statement);
    }

    default void debug(Consumer<T> statement) {
        debug("", null, statement);
    }

    default void warn(Consumer<T> statement) {
        warn("", null, statement);
    }

    default void info(Consumer<T> statement) {
        info("", null, statement);
    }

    default void error(Consumer<T> statement) {
        error("", null, statement);
    }

    default void trace(String mainMessage, Consumer<T> statement)
    {
        trace(mainMessage, null, statement);
    }

    default void debug(String mainMessage, Consumer<T> statement)
    {
        debug(mainMessage, null, statement);
    }

    default void warn(String mainMessage, Consumer<T> statement)
    {
        warn(mainMessage, null, statement);
    }

    default void info(String mainMessage, Consumer<T> statement)
    {
        info(mainMessage, null, statement);
    }

    default void error(String mainMessage, Consumer<T> statement)
    {
        error(mainMessage, null, statement);
    }

    void trace(String mainMessage, Throwable t, Consumer<T> statement);
    void debug(String mainMessage, Throwable t, Consumer<T> statement);
    void warn(String mainMessage, Throwable t, Consumer<T> statement);
    void info(String mainMessage, Throwable t, Consumer<T> statement);
    void error(String mainMessage, Throwable t, Consumer<T> statement);
}
