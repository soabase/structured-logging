/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.structured.logger;

import io.soabase.structured.logger.annotations.LoggerSchema;
import io.soabase.structured.logger.annotations.LoggerSchemas;
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;

@LoggerSchemas({
        @LoggerSchema(value = {Id.class, Code.class}, schemaName = "IdCodeSchema"),
        @LoggerSchema(value = {Id.class, Event.class}, schemaName = "IdEventSchema"),
        @LoggerSchema(value = Id.class, schemaName = "IdSchema"),
        @LoggerSchema(value = {Id.class, CustomSchema.class}, schemaName = "IdCustomSchema")
})
public class SampleSchemaFactory {
    private SampleSchemaFactory() {
    }
}
