package io.soabase.structured.logger.schemas;

public interface Code<R extends Code<R>> {
    R code(String type);
}
