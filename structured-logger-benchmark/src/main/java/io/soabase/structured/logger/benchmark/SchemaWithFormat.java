package io.soabase.structured.logger.benchmark;

import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;

public interface SchemaWithFormat extends Id<SchemaWithFormat>, Qty<SchemaWithFormat>, Time<SchemaWithFormat>, WithFormat<SchemaWithFormat> {
}
