package io.soabase.structured.logger.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class StandardLoggerBenchmark {
    @Benchmark
    public void test() {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.trace("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.warn("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.debug("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.error("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
        logger.info("message id={} qty={} time={}", Utils.str(), Utils.value(), Instant.now());
    }
}
