package io.soabase.structured.logger.util;

import io.soabase.structured.logger.schemas.Required;

public interface RequiredId<R extends RequiredId<R>> {
    @Required
    R id(int i);
}
