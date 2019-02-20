package io.soabase.structured.logger.slf4j;

import io.soabase.structured.logger.schemas.WithFormat;

public interface SchemaWithFormat extends WithFormat<SchemaWithFormat> {
    SchemaWithFormat a(String value);

    SchemaWithFormat b(String value);
}
