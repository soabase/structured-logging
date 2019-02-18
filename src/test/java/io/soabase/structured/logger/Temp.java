package io.soabase.structured.logger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Temp {
    @Test
    public void testBasic()
    {
        Logger logger = LoggerFactory.getLogger(getClass());
        // TODO make the StructuredLogger injectable, etc.
        StructuredLogger<TestSchema2> log = StructuredLoggerFactory.structured(logger, TestSchema2.class);
        log.debug(s -> s.context("one").event("two").id("three"));
    }

    @Test
    public void testException() {
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.structured(TestSchema.class);
        log.debug("Hey", new Error("what"), s -> s.context("one").event("two").id("three").catchAll("hey", "there"));
    }
}
