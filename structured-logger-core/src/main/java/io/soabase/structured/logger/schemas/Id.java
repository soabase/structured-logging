package io.soabase.structured.logger.schemas;

public interface Id<R extends Id<R>> {
    R id(String value);
}
