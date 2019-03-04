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

    @Override
    public Schema configValue(String value) {
        return this;
    }
}
