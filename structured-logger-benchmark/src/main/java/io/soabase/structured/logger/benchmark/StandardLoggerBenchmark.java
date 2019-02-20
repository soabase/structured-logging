package io.soabase.structured.logger.benchmark;

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
