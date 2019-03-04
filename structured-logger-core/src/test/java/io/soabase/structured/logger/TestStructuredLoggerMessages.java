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

import io.soabase.structured.logger.formatting.DefaultLoggingFormatter;
import io.soabase.structured.logger.generation.Generator;
import io.soabase.structured.logger.schema.Schema;
import io.soabase.structured.logger.schema.TestSchemaFactory;
import io.soabase.structured.logger.util.RecordingLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.soabase.structured.logger.formatting.DefaultLoggingFormatter.Option.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TestStructuredLoggerMessages {
    private final RecordingLogger logger = new RecordingLogger();

    public interface LocalSchema {
        LocalSchema id(String id);
        LocalSchema count(int n);
    }

    public interface LocalCasedSchema {
        LocalCasedSchema oneTwo(int i);

        LocalCasedSchema codeStyle(String s);
    }

    @After
    @Before
    public void tearDown() {
        StructuredLoggerFactory.clearCache();
    }

    @Test
    public void testMainMessageIsLastUnquotedNoException() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter(MAIN_MESSAGE_IS_LAST));
        log.info("A Message", m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("info");
        assertThat(logger.entries.get(0).message).isEqualTo("count=10 id=123 A Message");
        assertThat(logger.entries.get(0).t).isNull();
    }

    @Test
    public void testMainMessageIsFirstUnquotedNoException() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter());
        log.info("A Message", m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("info");
        assertThat(logger.entries.get(0).message).isEqualTo("A Message count=10 id=123");
        assertThat(logger.entries.get(0).t).isNull();
    }

    @Test
    public void testMainMessageIsFirstQuotedEscapedNoException() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter(QUOTE_VALUES, ESCAPE_VALUES));
        log.info("A Message", m -> m.id("123-\"x\"").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("info");
        assertThat(logger.entries.get(0).message).isEqualTo("A Message count=\"10\" id=\"123-\\\"x\\\"\"");
        assertThat(logger.entries.get(0).t).isNull();
    }

    @Test
    public void testMainMessageIsFirstEscapedException() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter(ESCAPE_VALUES));
        RuntimeException e = new RuntimeException("hey");
        log.info("A Message", e, m -> m.id("a b c").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("info");
        assertThat(logger.entries.get(0).message).isEqualTo("A Message count=10 id=a\\ b\\ c");
        assertThat(logger.entries.get(0).t).isInstanceOf(RuntimeException.class);
        assertThat(logger.entries.get(0).t.getMessage()).isEqualTo("hey");
    }

    @Test
    public void testCustomFactory() {
        try {
            StructuredLoggerFactory.setSchemaFactory(new TestSchemaFactory());
            StructuredLogger<Schema> log = StructuredLoggerFactory.getLogger(logger, Schema.class);
            log.info(s -> s.id("temp"));
        } finally {
            StructuredLoggerFactory.setSchemaFactory(new Generator());
        }

        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("info");
        assertThat(logger.entries.get(0).message).isEqualTo("custom");
    }

    @Test
    public void testSnakeCase() {
        StructuredLogger<LocalCasedSchema> log = StructuredLoggerFactory.getLogger(logger, LocalCasedSchema.class, new DefaultLoggingFormatter(SNAKE_CASE));
        log.warn(s -> s.codeStyle("one").oneTwo(123));

        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).level).isEqualTo("warn");
        assertThat(logger.entries.get(0).message).isEqualTo("code_style=one one_two=123");
    }
}
