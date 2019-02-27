package io.soabase.structured.logger.spi;

import io.soabase.structured.logger.formatting.LoggingFormatter;

/**
 * Factory for managing schema instances
 */
public interface SchemaFactory {
    /**
     * Create a new schema wrapper for the given schema class. The purpose of this manager is to allocate individual schema
     * objects as needed for logging
     *
     * @param schemaClass schema class
     * @param classLoader class loader to use (if needed)
     * @param loggingFormatter logging formatter to use
     * @return instance
     */
    <T> SchemaMetaInstance<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter);

    /**
     * Clear any internal caches
     */
    void clearCache();
}
