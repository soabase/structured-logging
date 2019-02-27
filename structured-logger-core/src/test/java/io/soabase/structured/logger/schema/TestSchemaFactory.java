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
package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.spi.SchemaFactory;
import io.soabase.structured.logger.spi.SchemaMetaInstance;
import org.slf4j.Logger;

public class TestSchemaFactory implements SchemaFactory {
    @Override
    public <T> SchemaMetaInstance<T> generate(Class<T> schemaClass, ClassLoader classLoader, LoggingFormatter loggingFormatter) {
        return new SchemaMetaInstance<T>() {
            @Override
            public T newSchemaInstance() {
                //noinspection unchecked
                return (T)new SchemaImpl();
            }

            @Override
            public void apply(LevelLogger levelLogger, Logger logger, T instance, String mainMessage, Throwable t) {
                levelLogger.log(logger, "custom");
            }
        };
    }

    @Override
    public void clearCache() {
        // NOP
    }
}
