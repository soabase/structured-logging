package io.soabase.structured.logger.benchmark;

import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;

public interface Schema extends Id<Schema>, Qty<Schema>, Time<Schema> {
}
