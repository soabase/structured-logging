package io.soabase.structured.logger;

public interface TestSchema {
    TestSchema event(String s);

    TestSchema id(String id);

    TestSchema context(String c);

    TestSchema catchAll(String name, Object value);
}
