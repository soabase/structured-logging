package io.soabase.structured.logger.schemas;

public interface Code<R extends Event<R>> {
    R code(String type);
}
