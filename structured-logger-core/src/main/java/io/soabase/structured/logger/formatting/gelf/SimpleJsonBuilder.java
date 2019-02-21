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

import java.util.function.Function;

public class SimpleJsonBuilder implements JsonBuilder<StringBuilder> {
    private final String exceptionFieldName;
    private final Function<Throwable, String> exceptionFormatter;

    public SimpleJsonBuilder() {
        this("_exception", Throwable::toString);
    }

    public SimpleJsonBuilder(String exceptionFieldName) {
        this(exceptionFieldName, Throwable::toString);
    }

    public SimpleJsonBuilder(String exceptionFieldName, Function<Throwable, String> exceptionFormatter) {
        this.exceptionFieldName = '"' + exceptionFieldName + '"';
        this.exceptionFormatter = exceptionFormatter;
    }

    @Override
    public StringBuilder newObject() {
        return new StringBuilder();
    }

    @Override
    public void addField(StringBuilder object, String name, Object value) {
        checkFirst(object);
        object.append('"').append(name).append("\":");
        format(object, value);
    }

    @Override
    public void addExceptionField(StringBuilder object, Throwable e) {
        checkFirst(object);
        object.append(exceptionFieldName).append(':');
        format(object, exceptionFormatter.apply(e));
    }

    @Override
    public String finalizeToJson(StringBuilder object) {
        if (object.length() == 0) {
            object.append('{');
        }
        return object.append('}').toString();
    }

    private void checkFirst(StringBuilder object) {
        if (object.length() == 0) {
            object.append('{');
        } else {
            object.append(", ");
        }
    }

    private void format(StringBuilder object, Object value) {
        if (value == null) {
            object.append("null");
        } else if (value.getClass().isPrimitive() || (value instanceof Number) ) {
            object.append(value);
        } else {
            object.append('"');
            String str = value.toString();
            for ( int i = 0; i < str.length(); ++i ) {
                char c = str.charAt(i);
                switch (str.charAt(i)) {
                    case '"': {
                        object.append("\\\"");
                        break;
                    }

                    case '\r': {
                        object.append("\\r");
                        break;
                    }

                    case '\n': {
                        object.append("\\n");
                        break;
                    }

                    default: {
                        object.append(c);
                        break;
                    }
                }
            }
            object.append('"');
        }
    }
}
