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
package io.soabase.structured.logger;

import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.generation.Generator;
import io.soabase.structured.logger.spi.SchemaFactory;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

/**
 * Factory for getting structured loggers
 */
public class StructuredLoggerFactory {
    private static final Generator generator = new Generator();
    private static volatile LoggingFormatter defaultLoggingFormatter = LoggingFormatter.defaultLoggingFormatter;
    private static volatile Function<Class, ClassLoader> classloaderProc = Class::getClassLoader;
    private static volatile boolean requiredValuesEnabled = true;
    private static volatile SchemaFactory schemaFactory = new Generator();

    /**
     * An internal cache of generated schemas, etc. is maintained. This clears that cache
     */
    public static void clearCache() {
        schemaFactory.clearCache();
    }

    /**
     * Set the default logging formatter to use. Normally, it's {@link io.soabase.structured.logger.formatting.DefaultLoggingFormatter}
     *
     * @param defaultLoggingFormatter new formatter
     */
    public static void setDefaultLoggingFormatter(LoggingFormatter defaultLoggingFormatter) {
        StructuredLoggerFactory.defaultLoggingFormatter = Objects.requireNonNull(defaultLoggingFormatter);
    }

    /**
     * Return the current default logging formatter
     * @return default logging formatter
     */
    public static LoggingFormatter getDefaultLoggingFormatter() {
        return defaultLoggingFormatter;
    }

    /**
     * The code generator needs a classloader. By default it is the class loader of the schema class
     * being used
     *
     * @param classloaderProc new classloader functor
     */
    public static void setClassloaderProc(Function<Class, ClassLoader> classloaderProc) {
        StructuredLoggerFactory.classloaderProc = classloaderProc;
    }

    /**
     * Return the current classloader proc
     *
     * @return proc
     */
    public static Function<Class, ClassLoader> getClassloaderProc() {
        return classloaderProc;
    }

    /**
     * Change whether or not required values are validated
     *
     * @param requiredValuesEnabled true/false
     */
    public static void setRequiredValuesEnabled(boolean requiredValuesEnabled) {
        StructuredLoggerFactory.requiredValuesEnabled = requiredValuesEnabled;
    }

    /**
     * Returns whether or not required values are validated
     *
     * @return true/false
     */
    public static boolean requiredValuesEnabled() {
        return requiredValuesEnabled;
    }

    public static SchemaFactory getSchemaFactory() {
        return schemaFactory;
    }

    public static void setSchemaFactory(SchemaFactory schemaFactory) {
        StructuredLoggerFactory.schemaFactory = schemaFactory;
    }

    public static <T> StructuredLogger<T> getLogger(Logger logger, Class<T> schema, LoggingFormatter loggingFormatter) {
        SchemaMetaInstance<T> schemaMetaInstance = schemaFactory.generate(schema, classloaderProc.apply(schema), loggingFormatter);
        return new StructuredLoggerImpl<>(logger, schemaMetaInstance);
    }

    public static <T> StructuredLogger<T> getLogger(Class<T> schema) {
        return getLogger(LoggerFactory.getLogger(schema), schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> getLogger(Class<?> clazz, Class<T> schema) {
        return getLogger(LoggerFactory.getLogger(clazz), schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> getLogger(String name, Class<T> schema) {
        return getLogger(LoggerFactory.getLogger(name), schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> getLogger(Logger logger, Class<T> schema) {
        return getLogger(logger, schema, getDefaultLoggingFormatter());
    }

    public static <T> StructuredLogger<T> getLogger(Class<T> schema, LoggingFormatter loggingFormatter) {
        return getLogger(LoggerFactory.getLogger(schema), schema, loggingFormatter);
    }

    private StructuredLoggerFactory() {
    }
}
