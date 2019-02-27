/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
