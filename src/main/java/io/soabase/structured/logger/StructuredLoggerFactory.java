package io.soabase.structured.logger;

import io.soabase.structured.logger.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.BiFunction;

public class StructuredLoggerFactory {
    private static final Generator generator = new Generator();
    private static volatile boolean requireAllSchemaMethods = false;
    private static volatile LoggingFormatter loggingFormatter = LoggingFormatter.defaultLoggingFormatter;
    private static volatile BiFunction<Logger, Class, ClassLoader> classloaderProc = (logger, aClass) -> aClass.getClassLoader();

    public static <T> StructuredLogger<T> structured(Class<T> schema) {
        loggingFormatter = LoggingFormatter.defaultLoggingFormatter;
        return internalGetLogger(LoggerFactory.getLogger(schema), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Class<?> clazz, Class<T> schema) {
        return internalGetLogger(LoggerFactory.getLogger(clazz), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(String name, Class<T> schema) {
        return internalGetLogger(LoggerFactory.getLogger(name), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema) {
        return internalGetLogger(logger, schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        return internalGetLogger(logger, schema, loggingFormatter);
    }

    public static void clearCache() {
        generator.clearCache();
    }

    public static void setRequireAllSchemaMethods(boolean requireAllSchemaMethods) {
        StructuredLoggerFactory.requireAllSchemaMethods = requireAllSchemaMethods;
    }

    public static void setLoggingFormatter(LoggingFormatter loggingFormatter) {
        StructuredLoggerFactory.loggingFormatter = Objects.requireNonNull(loggingFormatter);
    }

    public static void setClassloaderProc(BiFunction<Logger, Class, ClassLoader> classloaderProc) {
        StructuredLoggerFactory.classloaderProc = classloaderProc;
    }

    private static <T> StructuredLogger<T> internalGetLogger(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        T schemaInstance = newSchemaInstance(logger, schema);
        return new StructuredLoggerImpl<>(logger, schemaInstance, loggingFormatter, requireAllSchemaMethods);
    }

    private static <T> T newSchemaInstance(Logger logger, Class<T> schema) {
        Class<T> generate = generator.generate(schema, classloaderProc.apply(logger, schema));
        T schemaInstance = null;
        try {
            schemaInstance = generate.newInstance();
        } catch (Exception e) {
            // TODO
        }
        return schemaInstance;
    }

    private StructuredLoggerFactory() {
    }
}
