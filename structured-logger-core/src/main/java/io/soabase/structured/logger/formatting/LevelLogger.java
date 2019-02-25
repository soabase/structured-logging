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
package io.soabase.structured.logger.formatting;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

public interface LevelLogger {
    Level getLevel();

    boolean isEnabled(Logger logger);

    void log(Logger logger, String msg);

    void log(Logger logger, String format, Object arg);

    void log(Logger logger, String format, Object arg1, Object arg2);

    void log(Logger logger, String format, Object... arguments);

    void log(Logger logger, String msg, Throwable t);

    void log(Logger logger, Marker marker, String msg);

    void log(Logger logger, Marker marker, String format, Object arg);

    void log(Logger logger, Marker marker, String format, Object arg1, Object arg2);

    void log(Logger logger, Marker marker, String format, Object... argArray);

    void log(Logger logger, Marker marker, String msg, Throwable t);
}
