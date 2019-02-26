/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.structured.logger.formatting.gelf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Maven dependency for this project does NOT include Jackson. You must manually add a compatible version
 * of Jackson to your project when using JacksonJsonBuilder
 */
@SuppressWarnings("WeakerAccess")
public class JacksonJsonBuilder implements JsonBuilder<ObjectNode> {
    private final ObjectMapper mapper;
    private final String exceptionFieldName;
    private final ExceptionFormatter exceptionFormatter;

    public JacksonJsonBuilder(ObjectMapper mapper) {
        this(mapper, "_exception", ExceptionFormatter.standard);
    }

    public JacksonJsonBuilder(ObjectMapper mapper, String exceptionFieldName) {
        this(mapper, exceptionFieldName, ExceptionFormatter.standard);
    }

    public JacksonJsonBuilder(ObjectMapper mapper, String exceptionFieldName, ExceptionFormatter exceptionFormatter) {
        this.mapper = mapper;
        this.exceptionFieldName = exceptionFieldName;
        this.exceptionFormatter = exceptionFormatter;
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
        object.putPOJO(exceptionFieldName, exceptionFormatter.format(e));
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
