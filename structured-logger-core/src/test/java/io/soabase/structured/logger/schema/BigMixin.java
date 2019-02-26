package io.soabase.structured.logger.schema;

import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;

import java.time.Instant;

public interface BigMixin extends Id, Event, Time, Code, Qty, WithFormat {
    @Override
    BigMixin code(String type);

    @Override
    BigMixin event(String type);

    @Override
    BigMixin id(String value);

    @Override
    BigMixin qty(int n);

    @Override
    BigMixin time(Instant i);

    @Override
    BigMixin formatted(String format, Object... arguments);
}
