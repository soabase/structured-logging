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
        Generated<T> generated = generator.generate(schema, classloaderProc.apply(schema), loggingFormatter);
        return new StructuredLoggerImpl<>(logger, generated);
    }

    private StructuredLoggerFactoryBase() {
    }
}
