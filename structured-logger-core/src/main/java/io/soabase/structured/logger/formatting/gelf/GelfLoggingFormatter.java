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
package io.soabase.structured.logger.formatting.gelf;

import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class GelfLoggingFormatter implements LoggingFormatter {
    private final String host;
    private final JsonBuilder jsonBuilder;
    private final Supplier<Long> timestampSupplier;

    public GelfLoggingFormatter(String host, JsonBuilder jsonBuilder) {
        this(host, jsonBuilder, () -> Instant.now().toEpochMilli());
    }

    public GelfLoggingFormatter(String host, JsonBuilder jsonBuilder, Supplier<Long> timestampSupplier) {
        this.host = host;
        this.jsonBuilder = jsonBuilder;
        this.timestampSupplier = timestampSupplier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void apply(LevelLogger levelLogger, Logger logger, List<String> schemaNames, Arguments arguments, String mainMessage, Throwable t) {
        Object obj = jsonBuilder.newObject();
        addStandardFields(obj, mainMessage, host, timestampSupplier.get());

        int index = 0;  // 0 is the message
        for (String name : schemaNames) {
            jsonBuilder.addField(obj, "_" + name, arguments.get(index++));
        }
        if (t != null) {
            jsonBuilder.addExceptionField(obj, t);
        }
        String json = jsonBuilder.finalizeToJson(obj);
        log(levelLogger, logger, json);
    }

    // Visible for testing
    protected void log(LevelLogger levelLogger, Logger logger, String json) {
        levelLogger.log(logger, json);
    }

    @SuppressWarnings("unchecked")
    protected void addStandardFields(Object jsonObject, Object mainMessage, String host, long timestamp) {
        jsonBuilder.addField(jsonObject, "version", "1.1");
        jsonBuilder.addField(jsonObject, "host", host);
        jsonBuilder.addField(jsonObject, "short_message", mainMessage);
        jsonBuilder.addField(jsonObject, "timestamp", timestamp);
    }
}
