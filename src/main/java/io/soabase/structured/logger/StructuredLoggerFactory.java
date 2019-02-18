package io.soabase.structured.logger;

import io.soabase.structured.logger.generator.Generator;
import org.slf4j.Logger;

public class StructuredLoggerFactory {
    private static final Generator generator = new Generator();

    public static <T> StructuredLogger<T> get(Logger logger, Class<T> schema) {
        return get(logger, schema, LoggingFormatter.defaultLoggingFormatter);
    }

    public static <T> StructuredLogger<T> get(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        T schemaInstance = newSchemaInstance(logger, schema);
        return new StructuredLoggerImpl<>(logger, schemaInstance, loggingFormatter);
    }

    public static void clearCache() {
        generator.clearCache();
    }

    private static <T> T newSchemaInstance(Logger logger, Class<T> schema) {
        Class<T> generate = generator.generate(schema, logger.getClass().getClassLoader());
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
