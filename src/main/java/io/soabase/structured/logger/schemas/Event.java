package io.soabase.structured.logger.schemas;

public interface Event<R extends Event<R>> {
    R event(String type);
}
