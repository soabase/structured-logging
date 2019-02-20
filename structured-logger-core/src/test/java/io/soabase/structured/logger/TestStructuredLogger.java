package io.soabase.structured.logger;

import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;
import io.soabase.structured.logger.util.TestLoggerFacade;
import org.junit.Test;

import static io.soabase.structured.logger.LoggingFormatter.defaultLoggingFormatter;
import static io.soabase.structured.logger.LoggingFormatter.requiringAllValues;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TestStructuredLogger {
    public interface BigMixin extends Id<BigMixin>, Event<BigMixin>, Time<BigMixin>, Code<BigMixin>, Qty<BigMixin>, WithFormat<BigMixin> {}

    public interface LocalSchema {
        LocalSchema id(String id);
        LocalSchema count(int n);
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
        StructuredLogger<BigMixin> log = StructuredLoggerFactoryBase.getLogger(new TestLoggerFacade(), BigMixin.class, requiringAllValues(defaultLoggingFormatter));
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
}
