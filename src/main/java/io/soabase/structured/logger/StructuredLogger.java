package io.soabase.structured.logger;

import org.slf4j.Logger;

import java.util.function.Consumer;

public interface StructuredLogger<T> {
    Logger logger();

    default void trace(Consumer<T> statement) {
        trace(statement, "");
    }

    default void debug(Consumer<T> statement) {
        debug(statement, "");
    }

    default void warn(Consumer<T> statement) {
        warn(statement, "");
    }

    default void info(Consumer<T> statement) {
        info(statement, "");
    }

    default void error(Consumer<T> statement) {
        error(statement, "");
    }

    void trace(Consumer<T> statement, String mainMessage);
    void debug(Consumer<T> statement, String mainMessage);
    void warn(Consumer<T> statement, String mainMessage);
    void info(Consumer<T> statement, String mainMessage);
    void error(Consumer<T> statement, String mainMessage);
}
