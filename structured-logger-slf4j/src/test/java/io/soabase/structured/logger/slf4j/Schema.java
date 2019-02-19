package io.soabase.structured.logger.slf4j;

public interface Schema {
    Schema event(String s);

    Schema id(String id);

    Schema context(String c);
}
