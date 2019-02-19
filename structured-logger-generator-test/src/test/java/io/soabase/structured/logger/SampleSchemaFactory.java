package io.soabase.structured.logger;

import io.soabase.structured.logger.annotations.LoggerSchema;
import io.soabase.structured.logger.annotations.LoggerSchemas;
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;

@LoggerSchemas({
        @LoggerSchema(value = {Id.class, Code.class}, schemaName = "IdCodeSchema"),
        @LoggerSchema(value = {Id.class, Event.class}, schemaName = "IdEventSchema")
})
public class SampleSchemaFactory {
    private SampleSchemaFactory() {
    }
}
