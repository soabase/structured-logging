package io.soabase.structured.logger.slf4j;

import io.soabase.structured.logger.LoggerFacade;
import io.soabase.structured.logger.LoggingFormatter;
import io.soabase.structured.logger.StructuredLogger;
import io.soabase.structured.logger.StructuredLoggerFactoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static io.soabase.structured.logger.StructuredLoggerFactoryBase.*;

public class StructuredLoggerFactory {
    public static <T> StructuredLogger<T> structured(Class<T> schema) {
        return structured(LoggerFactory.getLogger(schema), schema);
    }

    public static <T> StructuredLogger<T> structured(Class<?> clazz, Class<T> schema) {
        return structured(LoggerFactory.getLogger(clazz), schema);
    }

    public static <T> StructuredLogger<T> structured(String name, Class<T> schema) {
        return structured(LoggerFactory.getLogger(name), schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema) {
        return getLogger(wrap(logger), schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> structured(Class<T> schema, LoggingFormatter loggingFormatter) {
        return getLogger(wrap(LoggerFactory.getLogger(schema)), schema, loggingFormatter);
    }

    public static <T> StructuredLogger<T> structured(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        return getLogger(wrap(logger), schema, loggingFormatter);
    }

    public static void clearCache() {
        StructuredLoggerFactoryBase.clearCache();
    }

    public static void setDefaultLoggingFormatter(LoggingFormatter defaultLoggingFormatter) {
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(defaultLoggingFormatter);
    }

    public static void setClassloaderProc(Function<Class, ClassLoader> classloaderProc) {
        StructuredLoggerFactoryBase.setClassloaderProc(classloaderProc);
    }

    private static LoggerFacade wrap(Logger logger) {
        return new Slf4jLoggerFacade(logger);
    }
}
