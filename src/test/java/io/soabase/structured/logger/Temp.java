package io.soabase.structured.logger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Temp {
    @Test
    public void temp()
    {
        Logger logger = LoggerFactory.getLogger(getClass());
        // TODO make the StructuredLogger injectable, etc.
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.get(logger, TestSchema.class);
        log.debug(s -> s.context("one").event("two").id("three").catchAll("hey", "there"));
    }
}
