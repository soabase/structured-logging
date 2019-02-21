package io.soabase.structured.logger;

import io.soabase.structured.logger.formatting.LoggingFormatter;
import io.soabase.structured.logger.formatting.gelf.GelfLoggingFormatter;
import io.soabase.structured.logger.formatting.gelf.SimpleJsonBuilder;
import io.soabase.structured.logger.util.Schema;
import io.soabase.structured.logger.util.TestLoggerFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.soabase.structured.logger.formatting.LoggingFormatter.defaultLoggingFormatter;

public class TestGelfFormatting {
    private static final LoggingFormatter gelfLoggingFormatter = new GelfLoggingFormatter("test", new SimpleJsonBuilder());

    @Before
    @After
    public void tearDown() {
        StructuredLoggerFactoryBase.clearCache();
        StructuredLoggerFactoryBase.setDefaultLoggingFormatter(defaultLoggingFormatter);
    }

    @Test
    public void testBasicFormatting() {
        TestLoggerFacade logger = new TestLoggerFacade();
        StructuredLogger<Schema> log = StructuredLoggerFactoryBase.getLogger(logger, Schema.class, gelfLoggingFormatter);
        log.debug("message", new Exception("hey"), m -> m.id("123"));
        System.out.println();
    }
}
