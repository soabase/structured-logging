package io.soabase.structured.logger.formatting.gelf;

public interface JsonBuilder<J> {
    J newObject();

    void addField(J object, String name, Object value);

    void addExceptionField(J object, Throwable e);

    String finalizeToJson(J object);
}
