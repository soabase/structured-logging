package io.soabase.structured.logger;

import io.soabase.structured.logger.schemas.WithCustom;

public interface TestSchemaWithCustom extends WithCustom<TestSchemaWithCustom> {
    TestSchemaWithCustom a(String value);

    TestSchemaWithCustom b(String value);
}
