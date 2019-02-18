package io.soabase.structured.logger;

import io.soabase.structured.logger.generation.Generated;
import io.soabase.structured.logger.generation.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class StructuredLoggerFactory {
    private static final Generator generator = new Generator();
    private static final Map<Class<?>, LoggingFormatter> registrations = new ConcurrentHashMap<>();
    private static volatile LoggingFormatter loggingFormatter = LoggingFormatter.defaultLoggingFormatter;
    private static volatile BiFunction<Logger, Class, ClassLoader> classloaderProc = (logger, aClass) -> aClass.getClassLoader();

    public static <T> StructuredLogger<T> structured(Class<T> schema) {
        return structured(LoggerFactory.getLogger(schema), schema);
    }

    public static <T> StructuredLogger<T> structured(Class<?> clazz, Class<T> schema) {
        return structured(LoggerFactory.getLogger(clazz), schema);
    }

    public static <T> StructuredLogger<T> structured(String name, Class<T> schema) {
        return structured(LoggerFactory.getLogger(name), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema) {
        @SuppressWarnings("unchecked")
        LoggingFormatter registered = registrations.get(schema);
        if (registered != null) {
            return internalGetLogger(logger, schema, registered);
        }
        return internalGetLogger(logger, schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Class<T> schema, LoggingFormatter loggingFormatter) {
        return internalGetLogger(LoggerFactory.getLogger(schema), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        return internalGetLogger(logger, schema, loggingFormatter);
    }

    public static <T> void register(Class<T> schema, LoggingFormatter loggingFormatter) {
        registrations.put(schema, loggingFormatter);
    }

    public static void clearCache() {
        generator.clearCache();
    }

    public static void clearRegistrations() {
        registrations.clear();
    }

    public static void setLoggingFormatter(LoggingFormatter loggingFormatter) {
        StructuredLoggerFactory.loggingFormatter = Objects.requireNonNull(loggingFormatter);
    }

    public static void setClassloaderProc(BiFunction<Logger, Class, ClassLoader> classloaderProc) {
        StructuredLoggerFactory.classloaderProc = classloaderProc;
    }

    private static <T> StructuredLogger<T> internalGetLogger(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        Generated<T> generated;
        T schemaInstance;
        try {
            generated = generator.generate(schema, classloaderProc.apply(logger, schema), loggingFormatter);
            schemaInstance = generated.generated().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not complete logging", e);
        }
        return new StructuredLoggerImpl<>(logger, schemaInstance, generated);
    }

    private StructuredLoggerFactory() {
    }
}
