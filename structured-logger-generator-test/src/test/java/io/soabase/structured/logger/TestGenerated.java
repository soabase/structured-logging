package io.soabase.structured.logger;

import io.soabase.structured.logger.annotations.LoggerSchema;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.slf4j.StructuredLoggerFactory;
import org.junit.Test;

@LoggerSchema(value={Id.class, Qty.class}, schemaName = "TestSchema") // <--- Preprocessor generates a "schema" class named TestSchema
public class TestGenerated {
    @Test
    public void testGenerated() {
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.structured(TestSchema.class);
        log.info("hey", schema -> schema.id("my id").qty(100));
    }
}
