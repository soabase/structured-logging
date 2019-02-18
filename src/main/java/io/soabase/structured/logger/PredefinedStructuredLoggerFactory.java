package io.soabase.structured.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PredefinedStructuredLoggerFactory {
    private static final Map<Integer, StructuredLogger> predefined = new ConcurrentHashMap<>();

    public static <T> void predefine(Logger logger, Class<T> schema) {
        predefine(logger, schema, LoggingFormatter.defaultLoggingFormatter);
    }

    public static <T> void predefine(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        int identityHashCode = System.identityHashCode(logger);
        predefined.computeIfAbsent(identityHashCode, __ -> StructuredLoggerFactory.get(logger, schema, loggingFormatter));
    }

    @SuppressWarnings("unchecked")
    public static <T> StructuredLogger<T> get(Logger logger) {
        return (StructuredLogger<T>)Objects.requireNonNull(predefined.get(System.identityHashCode(logger)));
    }

    public static <T> StructuredLogger<T> get(Class<?> clazz) {
        return get(LoggerFactory.getLogger(clazz));
    }

    public static <T> StructuredLogger<T> get(String name) {
        return get(LoggerFactory.getLogger(name));
    }

    public static void clear() {
    }

    private PredefinedStructuredLoggerFactory() {
    }
}
