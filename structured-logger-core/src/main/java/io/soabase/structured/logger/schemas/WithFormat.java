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
package io.soabase.structured.logger.schemas;

/**
 * Special purpose mixin. The Structured Logger treats WithFormat specially. It is translated
 * into a call to {@link String#format(String, Object...)}. E.g.:
 *
 * <code><pre>
 *  log.info(s -> s.formatted("a: %s, b: %s", anA, aB));
 *
 *  // is the same as with a normal schema value named "formatted" ala...
 *
 *  log.info(s -> s.formatted(String.format("a: %s, b: %s", anA, aB));
 * </pre></code>
 */
public interface WithFormat {
    WithFormat formatted(String format, Object... arguments);
}
