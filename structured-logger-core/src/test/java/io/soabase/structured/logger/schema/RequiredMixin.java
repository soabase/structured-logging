package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.annotations.Required;
import io.soabase.structured.logger.schemas.Code;

public interface RequiredMixin extends RequiredId, Code {
    @Override
    @Required
    RequiredId id(int i);

    @Override
    RequiredMixin code(String type);
}
