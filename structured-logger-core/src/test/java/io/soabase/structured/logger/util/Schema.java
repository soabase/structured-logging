package io.soabase.structured.logger.util;

public interface Schema {
    Schema event(String s);

    Schema id(String id);

    Schema context(String c);
}
