package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.TestStructuredLoggerArguments;

public interface AnotherId<R extends AnotherId<R>> {
    R id(String value);
}
