package io.soabase.structured.logger.benchmark;

import io.soabase.structured.logger.StructuredLogger;
import io.soabase.structured.logger.slf4j.StructuredLoggerFactory;
import org.openjdk.jmh.annotations.Benchmark;

import java.time.Instant;

public class StructuredLoggerBenchmark {
    @Benchmark
    public void test() {
        StructuredLogger<Schema> logger = StructuredLoggerFactory.structured(Schema.class);
        logger.trace("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.warn("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.debug("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.error("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
        logger.info("message", schema -> schema.id(Utils.str()).qty(Utils.value()).time(Instant.now()));
    }
}
