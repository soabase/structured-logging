package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.TestStructuredLoggerArguments;
import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;

public interface BigMixin extends Id<BigMixin>, Event<BigMixin>, Time<BigMixin>, Code<BigMixin>, Qty<BigMixin>, WithFormat<BigMixin> {}
