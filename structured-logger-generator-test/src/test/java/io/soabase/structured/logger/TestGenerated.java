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
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

@LoggerSchema(value={Id.class, Qty.class}, schemaName = "TestSchema") // <--- Preprocessor generates a "schema" class named TestSchema
public class TestGenerated {
    @Before
    @After
    public void setup() {
        StructuredLoggerFactory.clearCache();
    }

    @Test
    public void testGenerated() {
        StructuredLogger<TestSchema> log = StructuredLoggerFactory.getLogger(TestSchema.class);
        log.info("hey", schema -> schema.id("my id").qty(100));
    }

    @Test
    public void testFactoryGenerated() {
        StructuredLogger<IdCustomSchema> log = StructuredLoggerFactory.getLogger(IdCustomSchema.class);
        log.warn(schema -> schema.value(BigInteger.TEN).id("XYZZY"));
    }
}
