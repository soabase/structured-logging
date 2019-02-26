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
package io.soabase.structured.logger.benchmark.internals;

import io.soabase.structured.logger.schemas.Id;
import io.soabase.structured.logger.schemas.Qty;
import io.soabase.structured.logger.schemas.Time;

import java.time.Instant;

public interface Schema extends Id, Qty, Time {
    @Override
    Schema id(String value);

    @Override
    Schema qty(int n);

    @Override
    Schema time(Instant i);
}
