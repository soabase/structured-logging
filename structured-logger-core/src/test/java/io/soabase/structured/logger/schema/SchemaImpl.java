package io.soabase.structured.logger.schema;

public class SchemaImpl implements Schema {
    @Override
    public Schema event(String s) {
        return this;
    }

    @Override
    public Schema id(String s) {
        return this;
    }

    @Override
    public Schema context(String c) {
        return this;
    }

    @Override
    public Schema count(int c) {
        return this;
    }
}
