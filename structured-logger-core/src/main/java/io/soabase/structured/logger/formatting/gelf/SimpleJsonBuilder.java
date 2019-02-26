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

/**
 * In case you can't or don't want to use Jackson, this minimal JSON builder should suffice
 */
public class SimpleJsonBuilder implements JsonBuilder<StringBuilder> {
    private final String exceptionFieldName;
    private final ExceptionFormatter exceptionFormatter;

    public SimpleJsonBuilder() {
        this("_exception", ExceptionFormatter.standard);
    }

    public SimpleJsonBuilder(String exceptionFieldName) {
        this(exceptionFieldName, ExceptionFormatter.standard);
    }

    public SimpleJsonBuilder(String exceptionFieldName, ExceptionFormatter exceptionFormatter) {
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
        format(object, exceptionFormatter.format(e));
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
