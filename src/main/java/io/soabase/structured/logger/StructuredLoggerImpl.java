package io.soabase.structured.logger;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class StructuredLoggerImpl<T> implements StructuredLogger<T> {
    private final Logger logger;
    private final T schemaInstance;
    private final Generated<T> generated;
    private final boolean requireAllSchemaMethods;

    StructuredLoggerImpl(Logger logger, T schemaInstance, Generated<T> generated, boolean requireAllSchemaMethods) {
        this.logger = logger;
        this.schemaInstance = schemaInstance;
        this.generated = generated;
        this.requireAllSchemaMethods = requireAllSchemaMethods;
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
        if (requireAllSchemaMethods && values.values().stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Entire schema must be specified");
        }

        String formatString;
        List<String> names;
        if (generated.hasCustom()) {
            HashSet<String> generatedSet = new HashSet<>(generated.names());
            names = new ArrayList<>(generated.names());
            values.keySet().stream().filter(n -> !generatedSet.contains(n)).forEach(names::add);
            formatString = generated.loggingFormatter().buildFormatString(names);
        } else {
            names = Collections.emptyList();
            formatString = generated.formatString();
        }

        int argumentQty = names.size() + 1 + names.size();
        if (t != null) {
            argumentQty += 1;
        }

        Object[] arguments = new Object[argumentQty];
        int argumentIndex = 0;
        for (String name : names) {
            arguments[argumentIndex++] = values.get(name);
        }
        arguments[argumentIndex++] = mainMessage;

        if (t != null) {
            arguments[argumentIndex] = t;
        }
        logger.accept(formatString, arguments);
    }
}
