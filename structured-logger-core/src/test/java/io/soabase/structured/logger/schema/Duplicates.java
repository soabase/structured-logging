package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.TestStructuredLoggerArguments;
import io.soabase.structured.logger.schemas.Id;

public interface Duplicates extends Id<Duplicates>, AnotherId<Duplicates> {}
