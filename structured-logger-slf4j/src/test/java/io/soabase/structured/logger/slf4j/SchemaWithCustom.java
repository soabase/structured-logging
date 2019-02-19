package io.soabase.structured.logger.slf4j;

import io.soabase.structured.logger.schemas.WithCustom;

public interface SchemaWithCustom extends WithCustom<SchemaWithCustom> {
    SchemaWithCustom a(String value);

    SchemaWithCustom b(String value);
}
