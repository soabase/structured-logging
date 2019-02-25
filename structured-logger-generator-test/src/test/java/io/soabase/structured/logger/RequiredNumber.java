package io.soabase.structured.logger;

import io.soabase.structured.logger.schemas.Required;

public interface RequiredNumber<R extends RequiredNumber<R>> {
    @Required
    R number(Number n);
}
