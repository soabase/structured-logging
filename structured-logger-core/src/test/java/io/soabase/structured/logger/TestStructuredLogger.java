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
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;
import io.soabase.structured.logger.util.TestLoggerFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static io.soabase.structured.logger.LoggingFormatter.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TestStructuredLogger {
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
        StructuredLoggerFactoryBase.clearCache();
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(defaultLoggingFormatter);
    }

    @Test
    public void testBasic() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.debug("message", new Exception("hey"), m -> m.id("123").count(100));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).type).isEqualTo("debug");
        assertThat(logger.entries.get(0).arguments).hasSize(4);
        assertThat(logger.entries.get(0).arguments).contains("123");
        assertThat(logger.entries.get(0).arguments).contains(100);
        assertThat(logger.entries.get(0).arguments[2]).isEqualTo("message");
        assertThat(logger.entries.get(0).arguments[3]).isInstanceOf(Exception.class);
    }

    @Test(expected = MissingSchemaValueException.class)
    public void testMissingValue() {
        StructuredLogger<BigMixin> log = StructuredLoggerFactoryBase.getLogger(new TestLoggerFacade(), BigMixin.class, requireAllValues(defaultLoggingFormatter));
        log.info(m -> m.code("code-123"));
    }

    @Test
    public void testMissingValueNotEnabled() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(m -> m.id("123"));  // no error expected
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).type).isEqualTo("info");
        assertThat(logger.entries.get(0).arguments).hasSize(3);
        assertThat(logger.entries.get(0).arguments).contains("123");
        assertThat(logger.entries.get(0).arguments).contains((Integer)null);
        assertThat(logger.entries.get(0).arguments).contains("");
    }

    @Test
    public void testMainMessageIsFirstNoException() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, mainMessageIsFirst(defaultLoggingFormatter));
        log.info("A Message", m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments[0]).isEqualTo("A Message");
        assertThat(logger.entries.get(0).arguments[1]).isEqualTo(10);
        assertThat(logger.entries.get(0).arguments[2]).isEqualTo("123");
    }

    @Test
    public void testMainMessageIsFirstWithException() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, mainMessageIsFirst(defaultLoggingFormatter));
        log.info("A Message", new Exception(), m -> m.id("123").count(10));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments[0]).isEqualTo("A Message");
        assertThat(logger.entries.get(0).arguments[1]).isEqualTo(10);
        assertThat(logger.entries.get(0).arguments[2]).isEqualTo("123");
        assertThat(logger.entries.get(0).arguments[3]).isInstanceOf(Exception.class);
    }

    @Test
    public void testEmptySchema() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<Empty> log = StructuredLoggerFactoryBase.getLogger(logger, Empty.class, defaultLoggingFormatter);
        log.info("test", e -> {});
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).arguments[0]).isEqualTo("test");
    }

    @Test(expected = InvalidSchemaException.class)
    public void testDuplicateMixins() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLoggerFactoryBase.getLogger(logger, Duplicates.class, defaultLoggingFormatter);
    }

    @Test
    public void testCachingOfGenerated() {
        Set<Integer> ids = new HashSet<>();
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        assertThat(ids).hasSize(1);

        ids.clear();
        StructuredLoggerFactoryBase.clearCache();
        log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        StructuredLoggerFactoryBase.clearCache();
        log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        StructuredLoggerFactoryBase.clearCache();
        log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, defaultLoggingFormatter);
        log.info(s -> ids.add(System.identityHashCode(s.getClass())));
        assertThat(ids).hasSize(3);
    }

    @Test(expected = InvalidSchemaException.class)
    public void testBadReturnType() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLoggerFactoryBase.getLogger(logger, BadReturnType.class, defaultLoggingFormatter);
    }

    @Test
    public void testMixedBase() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLoggerFactoryBase.getLogger(logger, MixedBase.class, defaultLoggingFormatter);
    }

    @Test
    public void testSetDefaultLoggingFormatter() {
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(__ -> "TEST");
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<LocalSchema> log = StructuredLoggerFactoryBase.getLogger(logger, LocalSchema.class, StructuredLoggerFactoryBase.getDefaultLoggingFormatter());
        log.info(s -> s.id("an id").count(100));
        assertThat(logger.entries).hasSize(1);
        assertThat(logger.entries.get(0).message).isEqualTo("TEST");
    }
}
