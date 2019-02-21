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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.formatting.gelf.GelfLoggingFormatter;
import io.soabase.structured.logger.formatting.gelf.JacksonJsonBuilder;
import io.soabase.structured.logger.formatting.gelf.SimpleJsonBuilder;
import io.soabase.structured.logger.util.Schema;
import io.soabase.structured.logger.util.TestLoggerFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.soabase.structured.logger.formatting.LoggingFormatter.defaultLoggingFormatter;
import static org.assertj.core.api.Assertions.assertThat;

public class TestGelfFormatting {
    private static final LoggingFormatter gelfLoggingFormatter = new GelfLoggingFormatter("test", new SimpleJsonBuilder(), () -> 2468L);

    @Before
    @After
    public void tearDown() {
        StructuredLoggerFactoryBase.clearCache();
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(defaultLoggingFormatter);
    }

    @Test
    public void testBasicFormatting() throws IOException {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<Schema> log = StructuredLoggerFactoryBase.getLogger(logger, Schema.class, gelfLoggingFormatter);
        log.debug("message", new Exception("hey"), m -> m.id("123").context(null).event("y").count(456));
        validate(logger);
    }

    @Test
    public void testEscapingAndMultiLine() throws IOException {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<Schema> log = StructuredLoggerFactoryBase.getLogger(logger, Schema.class, gelfLoggingFormatter);
        log.debug("message", new Exception("hey"), m -> m.id("123").context(null).event("y").count(456));
        validate(logger);
    }

    @Test
    public void testJacksonMapper() {
        GelfLoggingFormatter formatter = new GelfLoggingFormatter("test", new JacksonJsonBuilder(new ObjectMapper()));
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<Schema> log = StructuredLoggerFactoryBase.getLogger(logger, Schema.class, formatter);
        log.debug("message", m -> m.context("\"quoted\"").event("line1\nline2"));
        System.out.println();
    }

    private void validate(TestLoggerFacade logger) throws IOException {
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments).isNull();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(logger.entries.get(0).message);
        assertThat(tree.get("version")).isNotNull();
        assertThat(tree.get("version").asText()).isEqualTo("1.1");
        assertThat(tree.get("short_message")).isNotNull();
        assertThat(tree.get("short_message").asText()).isEqualTo("message");
        assertThat(tree.get("timestamp")).isNotNull();
        assertThat(tree.get("timestamp").asLong()).isEqualTo(2468L);
        assertThat(tree.get("host")).isNotNull();
        assertThat(tree.get("host").asText()).isEqualTo("test");
        assertThat(tree.get("_id")).isNotNull();
        assertThat(tree.get("_id").asText()).isEqualTo("123");
        assertThat(tree.get("_context")).isNotNull();
        assertThat(tree.get("_context").isNull()).isTrue();
        assertThat(tree.get("_event")).isNotNull();
        assertThat(tree.get("_event").asText()).isEqualTo("y");
        assertThat(tree.get("_count")).isNotNull();
        assertThat(tree.get("_count").asInt()).isEqualTo(456);
        assertThat(tree.get("_exception")).isNotNull();
        assertThat(tree.get("_exception").asText()).isEqualTo("java.lang.Exception: hey");
    }
}
