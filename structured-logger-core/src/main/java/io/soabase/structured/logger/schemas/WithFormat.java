package io.soabase.structured.logger.schemas;

public interface WithFormat<R extends WithFormat<R>> {
    R formatted(String format, Object... arguments);
}
