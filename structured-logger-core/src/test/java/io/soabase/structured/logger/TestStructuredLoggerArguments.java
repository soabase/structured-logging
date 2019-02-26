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

import io.soabase.structured.logger.exception.InvalidSchemaException;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.schema.BadReturnType;
import io.soabase.structured.logger.schema.Duplicates;
import io.soabase.structured.logger.schema.Empty;
import io.soabase.structured.logger.schema.MixedBase;
import io.soabase.structured.logger.schema.RequiredMixin;
import io.soabase.structured.logger.util.RecordingLoggingFormatter;
import io.soabase.structured.logger.schema.SchemaWithSort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TestStructuredLoggerArguments {
    private final RecordingLoggingFormatter loggingFormatter = new RecordingLoggingFormatter();

    public interface LocalSchema {
        LocalSchema id(String id);
        LocalSchema count(int n);
    }

    @After
    @Before
    public void tearDown() {
        StructuredLoggerFactory.clearCache();
    }

    @Test
    public void testBasic() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(LocalSchema.class, loggingFormatter);
        log.debug("message", new Exception("hey"), m -> m.id("123").count(100));
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).levelLogger.getLevel()).isEqualTo(Level.DEBUG);
        assertThat(loggingFormatter.entries.get(0).arguments.size()).isEqualTo(2);
        assertThat(loggingFormatter.entries.get(0).arguments.get(0)).isEqualTo(100);
        assertThat(loggingFormatter.entries.get(0).arguments.get(1)).isEqualTo("123");
        assertThat(loggingFormatter.entries.get(0).mainMessage).isEqualTo("message");
        assertThat(loggingFormatter.entries.get(0).t).isInstanceOf(Exception.class);
    }

    @Test(expected = MissingSchemaValueException.class)
    public void testMissingValue() {
        StructuredLogger<RequiredMixin> log = StructuredLoggerFactory.getLogger(RequiredMixin.class);
        log.info(m -> m.code("code-123"));
    }

    @Test
    public void testMissingValueNotEnabled() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(LocalSchema.class, loggingFormatter);
        log.info(m -> m.id("123"));  // no error expected
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).levelLogger.getLevel()).isEqualTo(Level.INFO);
        assertThat(loggingFormatter.entries.get(0).arguments.size()).isEqualTo(2);
        assertThat(loggingFormatter.entries.get(0).arguments.get(0)).isNull();
        assertThat(loggingFormatter.entries.get(0).arguments.get(1)).isEqualTo("123");
    }

    @Test
    public void testEmptySchema() {
        StructuredLogger<Empty> log = StructuredLoggerFactory.getLogger(Empty.class, loggingFormatter);
        log.info("test", e -> {});
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).mainMessage).isEqualTo("test");
    }

    @Test(expected = InvalidSchemaException.class)
    public void testDuplicates() {
        StructuredLoggerFactory.getLogger(Duplicates.class);
    }

    @Test
    public void testCachingOfGenerated() {
        Set<Integer> ids = new HashSet<>();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        assertThat(ids).hasSize(1);

        ids.clear();
        StructuredLoggerFactory.clearCache();
        log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        StructuredLoggerFactory.clearCache();
        log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        StructuredLoggerFactory.clearCache();
        log = StructuredLoggerFactory.getLogger(LocalSchema.class);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        assertThat(ids).hasSize(3);
    }

    @Test(expected = InvalidSchemaException.class)
    public void testBadReturnType() {
        StructuredLoggerFactory.getLogger(BadReturnType.class);
    }

    @Test
    public void testMixedBase() {
        StructuredLoggerFactory.getLogger(MixedBase.class);
    }

    @Test
    public void testSetDefaultLoggingFormatter() {
        try {
            AtomicInteger counter = new AtomicInteger(0);
            StructuredLoggerFactory.setDefaultLoggingFormatter((levelLogger, logger, schemaNames, arguments, mainMessage, t) -> counter.incrementAndGet());
            StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(LocalSchema.class);
            log.info(s -> s.id("an id").count(100));
            assertThat(counter.get()).isEqualTo(1);
        } finally {
            StructuredLoggerFactory.setDefaultLoggingFormatter(LoggingFormatter.defaultLoggingFormatter);
        }
    }

    @Test
    public void testSorted() {
        StructuredLogger<SchemaWithSort> log = StructuredLoggerFactory.getLogger(SchemaWithSort.class, loggingFormatter);
        log.debug(s -> s.bool(true).id("hey").qty(100).zero(0));
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).arguments.get(0)).isEqualTo(0);
        assertThat(loggingFormatter.entries.get(0).arguments.get(1)).isEqualTo(100);
        assertThat(loggingFormatter.entries.get(0).arguments.get(2)).isEqualTo(true);
        assertThat(loggingFormatter.entries.get(0).arguments.get(3)).isEqualTo("hey");
    }
}
