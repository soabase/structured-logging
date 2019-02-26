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
 * Minimal interface for building JSON Objects.
 */
public interface JsonBuilder<J> {
    /**
     * Create a new empty JSON object
     *
     * @return new empty object
     */
    J newObject();

    /**
     * Add a field/value to the JSON object
     *
     * @param object object being built
     * @param name field name
     * @param value value
     */
    void addField(J object, String name, Object value);

    void addExceptionField(J object, Throwable e);

    String finalizeToJson(J object);
}
