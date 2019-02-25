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
import io.soabase.structured.logger.formatting.DefaultLoggingFormatter;
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;
import io.soabase.structured.logger.util.RecordingLoggingFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TestStructuredLogger {
    private final RecordingLoggingFormatter loggingFormatter = new RecordingLoggingFormatter();

    public interface BigMixin extends Id<BigMixin>, Event<BigMixin>, Time<BigMixin>, Code<BigMixin>, Qty<BigMixin>, WithFormat<BigMixin> {}

    public interface Empty {}

    public interface AnotherId<R extends AnotherId<R>> {
        R id(String value);
    }

    public interface Duplicates extends Id<Duplicates>, AnotherId<Duplicates> {}

    public interface LocalSchema {
        LocalSchema id(String id);
        LocalSchema count(int n);
    }

    public interface BadReturnType {
        String id(String id);
    }

    public interface Base1 {
        Base1 one(String i);
    }

    public interface Base2 {
        Base2 two(String i);
    }

    public interface MixedBase extends Base1, Base2 {}

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
        assertThat(loggingFormatter.entries.get(0).arguments).hasSize(2);
        assertThat(loggingFormatter.entries.get(0).arguments).contains("123");
        assertThat(loggingFormatter.entries.get(0).arguments).contains(100);
        assertThat(loggingFormatter.entries.get(0).mainMessage).isEqualTo("message");
        assertThat(loggingFormatter.entries.get(0).t).isInstanceOf(Exception.class);
    }

    @Test(expected = MissingSchemaValueException.class)
    public void testMissingValue() {
        StructuredLogger<BigMixin> log = StructuredLoggerFactory.getLogger(BigMixin.class, new DefaultLoggingFormatter(true, false, true));
        log.info(m -> m.code("code-123"));
    }

    @Test
    public void testMissingValueNotEnabled() {
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(LocalSchema.class, loggingFormatter);
        log.info(m -> m.id("123"));  // no error expected
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).levelLogger.getLevel()).isEqualTo(Level.INFO);
        assertThat(loggingFormatter.entries.get(0).arguments).hasSize(2);
        assertThat(loggingFormatter.entries.get(0).arguments).contains("123");
        assertThat(loggingFormatter.entries.get(0).arguments).contains((Integer)null);
    }
/*

    @Test
    public void testMainMessageIsFirstNoException() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter(false, false, true));
        log.info("A Message", m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments[0]).isEqualTo("A Message");
        assertThat(logger.entries.get(0).arguments[1]).isEqualTo(10);
        assertThat(logger.entries.get(0).arguments[2]).isEqualTo("123");
    }
*/
/*

    @Test
    public void testMainMessageIsFirstWithException() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactory.getLogger(logger, LocalSchema.class, new DefaultLoggingFormatter(false, false, true));
        log.info("A Message", new Exception(), m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments[0]).isEqualTo("A Message");
        assertThat(logger.entries.get(0).arguments[1]).isEqualTo(10);
        assertThat(logger.entries.get(0).arguments[2]).isEqualTo("123");
        assertThat(logger.entries.get(0).arguments[3]).isInstanceOf(Exception.class);
    }
*/

    @Test
    public void testEmptySchema() {
        StructuredLogger<Empty> log = StructuredLoggerFactory.getLogger(Empty.class, loggingFormatter);
        log.info("test", e -> {});
        assertThat(loggingFormatter.entries).hasSize(1);
        assertThat(loggingFormatter.entries.get(0).mainMessage).isEqualTo("test");
    }

    @Test(expected = InvalidSchemaException.class)
    public void testDuplicateMixins() {
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

/*
TODO

    @Test
    public void testSetDefaultLoggingFormatter() {
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(__ -> "TEST");
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, StructuredLoggerFactoryBase.getDefaultLoggingFormatter());
        log.info(s -> s.id("an id").count(100));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).message).isEqualTo("TEST");
    }
*/
}
