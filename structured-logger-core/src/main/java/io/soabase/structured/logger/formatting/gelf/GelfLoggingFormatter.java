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

import io.soabase.structured.logger.formatting.LoggingFormatter;

import java.time.Instant;
import java.util.Collection;
import java.util.function.BiConsumer;
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

    @Override
    public String buildFormatString(Collection<String> names) {
        return "";  // not used
    }

    @Override
    public boolean mainMessageIsLast() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void callConsumer(BiConsumer<String, Object[]> consumer, String format, Collection<String> names, Object[] arguments, boolean lastArgumentIsException) {
        Object obj = jsonBuilder.newObject();
        jsonBuilder.addField(obj, "version", "1.1");
        jsonBuilder.addField(obj, "host", host);
        jsonBuilder.addField(obj, "short_message", arguments[0]);
        jsonBuilder.addField(obj, "timestamp", timestampSupplier.get());
        int index = 1;  // 0 is the message
        for (String name : names) {
            jsonBuilder.addField(obj, "_" + name, arguments[index++]);
        }
        if (lastArgumentIsException) {
            jsonBuilder.addExceptionField(obj, (Throwable)arguments[index]);
        }
        consumer.accept("{}", new Object[]{jsonBuilder.finalizeToJson(obj)});
    }
}
