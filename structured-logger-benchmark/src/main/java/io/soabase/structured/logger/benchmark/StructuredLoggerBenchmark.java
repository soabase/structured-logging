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
import io.soabase.structured.logger.slf4j.StructuredLoggerFactory;
import org.openjdk.jmh.annotations.Benchmark;

import java.time.Instant;

public class StructuredLoggerBenchmark {
    private static final StructuredLogger<Schema> logger = StructuredLoggerFactory.structured(Schema.class);

    @Benchmark
    public void testFreshLogger() {
        StructuredLogger<Schema> newLogger = StructuredLoggerFactory.structured(Schema.class);
        newLogger.trace("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        newLogger.warn("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        newLogger.debug("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        newLogger.error("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        newLogger.info("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
    }

    @Benchmark
    public void testSavedLogger() {
        logger.trace("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.warn("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.debug("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.error("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.info("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
    }
}
