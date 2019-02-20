package io.soabase.structured.logger;

import io.soabase.structured.logger.generation.Generated;
import io.soabase.structured.logger.generation.Generator;

import java.util.Objects;
import java.util.function.Function;

public class StructuredLoggerFactoryBase {
    private static final Generator generator = new Generator();
    private static volatile LoggingFormatter defaultLoggingFormatter = LoggingFormatter.defaultLoggingFormatter;
    private static volatile Function<Class, ClassLoader> classloaderProc = Class::getClassLoader;

    public static void clearCache() {
        generator.clearCache();
    }

    public static void setDefaultLoggingFormatter(LoggingFormatter defaultLoggingFormatter) {
        StructuredLoggerFactoryBase.defaultLoggingFormatter = Objects.requireNonNull(defaultLoggingFormatter);
    }

    public static LoggingFormatter getDefaultLoggingFormatter() {
        return defaultLoggingFormatter;
    }

    public static void setClassloaderProc(Function<Class, ClassLoader> classloaderProc) {
        StructuredLoggerFactoryBase.classloaderProc = classloaderProc;
    }

    public static Function<Class, ClassLoader> getClassloaderProc() {
        return classloaderProc;
    }

    public static <T> StructuredLogger<T> getLogger(LoggerFacade logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        try {
            Generated<T> generated = generator.generate(schema, classloaderProc.apply(schema), loggingFormatter);
            return new StructuredLoggerImpl<>(logger, generated);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate schema for: " + schema.getName(), e);
        }
    }

    private StructuredLoggerFactoryBase() {
    }
}
