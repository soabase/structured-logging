package io.soabase.structured.logger;

import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithCustom;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class Temp {
    @Test
    public void testBasic()
    {
        Logger logger = LoggerFactory.getLogger(getClass());
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.structured(logger, TestSchema.class);
        log.debug(s -> s.context("one").event("two").id("three"));
    }

    @Test
    public void testException() {
        StructuredLogger<TestSchemaWithCustom> log = StructuredLoggerFactory.structured(TestSchemaWithCustom.class);
        log.debug("Hey", new Error("what"), s -> s.a("one").b("three").custom("hey", "there"));
    }

    public interface Mixin extends Id<Mixin>, Event<Mixin>, Time<Mixin>, Code<Mixin>, Qty<Mixin>, WithCustom<Mixin>{}

    @Test
    public void testMixin() {
        StructuredLogger<Mixin> log = StructuredLoggerFactory.structured(Mixin.class);
        log.info(m -> m.code("code-123").event("event-456").id("id-789").time(Instant.now()).custom("another", "thing").qty(100));
    }
}
