package io.soabase.structured.logger;

import io.soabase.structured.logger.generation.Generated;
import io.soabase.structured.logger.generation.Instance;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class StructuredLoggerImpl<T> implements StructuredLogger<T> {
    private final Logger logger;
    private final T schemaInstance;
    private final Generated<T> generated;

    StructuredLoggerImpl(Logger logger, T schemaInstance, Generated<T> generated) {
        this.logger = logger;
        this.schemaInstance = schemaInstance;
        this.generated = generated;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public void trace(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isTraceEnabled()) {
            consume(statement, mainMessage, t, logger::trace);
        }
    }

    @Override
    public void debug(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isDebugEnabled()) {
            consume(statement, mainMessage, t, logger::debug);
        }
    }

    @Override
    public void warn(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isWarnEnabled()) {
            consume(statement, mainMessage, t, logger::warn);
        }
    }

    @Override
    public void info(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isInfoEnabled()) {
            consume(statement, mainMessage, t, logger::info);
        }
    }

    @Override
    public void error(String mainMessage, Throwable t, Consumer<T> statement) {
        if (logger.isErrorEnabled()) {
            consume(statement, mainMessage, t, logger::error);
        }
    }

    private void consume(Consumer<T> statement, String mainMessage, Throwable t, BiConsumer<String, Object[]> logger) {
        statement.accept(schemaInstance);
        Map<String, Object> values = ((Instance) schemaInstance).slogGetValues();
        generated.apply(mainMessage, values, t, logger);
    }
}
