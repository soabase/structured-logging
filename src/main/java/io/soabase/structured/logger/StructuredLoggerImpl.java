package io.soabase.structured.logger;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

class StructuredLoggerImpl<T> implements StructuredLogger<T> {
    private final Logger logger;
    private final T schemaInstance;
    private final LoggingFormatter loggingFormatter;

    StructuredLoggerImpl(Logger logger, T schemaInstance, LoggingFormatter loggingFormatter) {
        this.logger = logger;
        this.schemaInstance = schemaInstance;
        this.loggingFormatter = loggingFormatter;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public void trace(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isTraceEnabled()) {
            consume(statement, mainMessage, (t != null) ? s -> logger.trace(s, t) : logger::trace);
        }
    }

    @Override
    public void debug(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isDebugEnabled()) {
            consume(statement, mainMessage, (t != null) ? s -> logger.debug(s, t) : logger::debug);
        }
    }

    @Override
    public void warn(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isWarnEnabled()) {
            consume(statement, mainMessage, (t != null) ? s -> logger.warn(s, t) : logger::warn);
        }
    }

    @Override
    public void info(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isInfoEnabled()) {
            consume(statement, mainMessage, (t != null) ? s -> logger.info(s, t) : logger::info);
        }
    }

    @Override
    public void error(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isErrorEnabled()) {
            consume(statement, mainMessage, (t != null) ? s -> logger.error(s, t) : logger::error);
        }
    }

    private void consume(Consumer<T> statement, String mainMessage, Consumer<String> logger)
    {
        statement.accept(schemaInstance);
        Map<String, Object> values = ((Instance) schemaInstance).slogGetValues();
        if (values.values().stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Entire schema must be specified");
        }
        logger.accept(loggingFormatter.format(mainMessage, values));
    }
}
