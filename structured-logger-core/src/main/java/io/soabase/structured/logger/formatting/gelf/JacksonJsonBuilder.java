package io.soabase.structured.logger.formatting.gelf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Maven dependency for this project does NOT include Jackson. You must manually add a compatible version
 * of Jackson to your project when using JacksonJsonBuilder
 */
public class JacksonJsonBuilder implements JsonBuilder<ObjectNode> {
    private final ObjectMapper mapper;
    private final String exceptionFieldName;

    public JacksonJsonBuilder(ObjectMapper mapper) {
        this(mapper, "_exception");
    }

    public JacksonJsonBuilder(ObjectMapper mapper, String exceptionFieldName) {
        this.mapper = mapper;
        this.exceptionFieldName = exceptionFieldName;
    }

    @Override
    public ObjectNode newObject() {
        return mapper.createObjectNode();
    }

    @Override
    public void addField(ObjectNode object, String name, Object value) {
        object.putPOJO(name, value);
    }

    @Override
    public void addExceptionField(ObjectNode object, Throwable e) {
        object.putPOJO(exceptionFieldName, e);
    }

    @Override
    public String finalizeToJson(ObjectNode object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
