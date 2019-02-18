package io.soabase.structured.logger.schemas;

import java.time.Instant;

public interface Time<R extends Time<R>> {
    R time(Instant i);
}
