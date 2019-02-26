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

import io.soabase.structured.logger.schemas.Code;
import io.soabase.structured.logger.schemas.Event;
import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;
import io.soabase.structured.logger.schemas.WithFormat;

import java.time.Instant;

public interface BigMixin extends Id, Event, Time, Code, Qty, WithFormat {
    @Override
    BigMixin code(String type);

    @Override
    BigMixin event(String type);

    @Override
    BigMixin id(String value);

    @Override
    BigMixin qty(int n);

    @Override
    BigMixin time(Instant i);

    @Override
    BigMixin formatted(String format, Object... arguments);
}
