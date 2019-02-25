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

import io.soabase.structured.logger.benchmark.internals.Utils;
import org.openjdk.jmh.annotations.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class StandardLoggerBenchmark {
    private static final Logger logger = LoggerFactory.getLogger(StandardLoggerBenchmark.class);

    @Benchmark
    public void testFreshLogger() {
        Logger newLogger = LoggerFactory.getLogger(getClass());
        newLogger.trace("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        newLogger.warn("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        newLogger.debug("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        newLogger.error("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        newLogger.info("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
    }

    @Benchmark
    public void testSavedLogger() {
        logger.trace("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.warn("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.debug("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.error("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.info("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
    }
}
