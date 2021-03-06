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
package io.soabase.structured.logger.util;

import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.formatting.LevelLogger;
import io.soabase.structured.logger.formatting.LoggingFormatter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RecordingLoggingFormatter implements LoggingFormatter {
    public final List<Entry> entries = new ArrayList<>();

    @Override
    public void apply(LevelLogger levelLogger, Logger logger, List<String> schemaNames, Arguments arguments, String mainMessage, Throwable t) {
        entries.add(new Entry(levelLogger, schemaNames, arguments, mainMessage, t));
    }

    public static class Entry {
        public final LevelLogger levelLogger;
        public final List<String> schemaNames;
        public final Arguments arguments;
        public final String mainMessage;
        public final Throwable t;

        public Entry(LevelLogger levelLogger, List<String> schemaNames, Arguments arguments, String mainMessage, Throwable t) {
            this.levelLogger = levelLogger;
            this.schemaNames = schemaNames;
            this.arguments = arguments;
            this.mainMessage = mainMessage;
            this.t = t;
        }
    }
}
