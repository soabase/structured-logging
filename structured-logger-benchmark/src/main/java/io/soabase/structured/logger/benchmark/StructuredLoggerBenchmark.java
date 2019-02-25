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
package io.soabase.structured.logger.benchmark;

import io.soabase.structured.logger.StructuredLogger;
import io.soabase.structured.logger.StructuredLoggerFactory;
import io.soabase.structured.logger.benchmark.internals.Schema;
import io.soabase.structured.logger.benchmark.internals.StubbedLogger;
import io.soabase.structured.logger.benchmark.internals.Utils;
import io.soabase.structured.logger.formatting.FastLoggingFormatter;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.openjdk.jmh.annotations.Benchmark;

import java.time.Instant;

public class StructuredLoggerBenchmark {
    private static final LoggingFormatter fastLoggingFormatter = new FastLoggingFormatter(false, true, false);

    //@Benchmark
    public void testFreshLoggerDefaultFormatter() {
        testAllLevels(null, LoggingFormatter.defaultLoggingFormatter);
    }

    //@Benchmark
    public void testSavedLoggerDefaultFormatter() {
        StructuredLogger<Schema> logger = StructuredLoggerFactory.structured(Schema.class, LoggingFormatter.defaultLoggingFormatter);
        testAllLevels(logger, LoggingFormatter.defaultLoggingFormatter);
    }

    @Benchmark
    public void testFreshLoggerFastFormatter() {
        testAllLevels(null, fastLoggingFormatter);
    }

    @Benchmark
    public void testSavedLoggerFastFormatter() {
        StructuredLogger<Schema> logger = StructuredLoggerFactory.structured(Schema.class, fastLoggingFormatter);
        testAllLevels(logger, fastLoggingFormatter);
    }

    @Benchmark
    public void testStubbedLogger() {
        StructuredLogger<Schema> logger = StructuredLoggerFactory.structured(new StubbedLogger(), Schema.class, fastLoggingFormatter);
        testAllLevels(logger, fastLoggingFormatter);
    }

    private void testAllLevels(StructuredLogger<Schema> logger, LoggingFormatter formatter) {
        if (logger == null) {
            logger = StructuredLoggerFactory.structured(Schema.class, formatter);
        }
        logger.trace("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.warn("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.debug("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.error("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.info("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
    }
}
