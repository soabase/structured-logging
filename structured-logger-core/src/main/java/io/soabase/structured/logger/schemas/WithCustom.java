package io.soabase.structured.logger.schemas;

public interface WithCustom<R extends WithCustom<R>> {
    R custom(String field, Object value);
}
