package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.spi.SchemaFactory;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import org.slf4j.Logger;

public class TestSchemaFactory implements SchemaFactory {
    @Override
    public <T> SchemaMetaInstance<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter) {
        return new SchemaMetaInstance<T>() {
            @Override
            public T newSchemaInstance() {
                //noinspection unchecked
                return (T)new SchemaImpl();
            }

            @Override
            public LoggingFormatter loggingFormatter() {
                return loggingFormatter;
            }

            @Override
            public void apply(LevelLogger levelLogger, Logger logger, T instance, String mainMessage, Throwable t) {
                levelLogger.log(logger, "custom");
            }
        };
    }

    @Override
    public void clearCache() {
        // NOP
    }
}
