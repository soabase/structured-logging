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
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.get(logger, TestSchema.class);
        log.debug(s -> s.context("one").event("two").id("three").catchAll("hey", "there"));
    }

    @Test
    public void testPredefined() {
        PredefinedStructuredLoggerFactory.predefine(LoggerFactory.getLogger(getClass()), TestSchema.class);

        StructuredLogger<TestSchema> log = PredefinedStructuredLoggerFactory.get(getClass());
        log.debug("Hey", new Error("what"), s -> s.context("one").event("two").id("three").catchAll("hey", "there"));
    }
}
