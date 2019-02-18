package io.soabase.structured.logger;

import io.soabase.structured.logger.annotations.LoggerSchema;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import org.junit.Test;

@LoggerSchema({Id.class, Qty.class})
public class TestGenerated {
    @Test
    public void testGenerated() {
        StructuredLogger<TestGeneratedSchema> log = StructuredLoggerFactory.structured(TestGeneratedSchema.class);
        log.info("hey", schema -> schema.id("my id").qty(100));
    }
}
