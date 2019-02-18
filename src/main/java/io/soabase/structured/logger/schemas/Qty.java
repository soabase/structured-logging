package io.soabase.structured.logger.schemas;

public interface Qty<R extends Qty<R>> {
    R qty(int n);
}
