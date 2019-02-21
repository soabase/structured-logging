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

import io.soabase.structured.logger.LoggerFacade;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestLoggerFacade implements LoggerFacade {
    public static class Entry {
        public final String type;
        public final String message;
        public final Object[] arguments;

        public Entry(String type, String message, Object[] arguments) {
            this.type = type;
            this.message = message;
            this.arguments = (arguments != null) ? Arrays.copyOf(arguments, arguments.length) : null;
        }
    }

    public final List<Entry> entries = new CopyOnWriteArrayList<>();

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void trace(String message) {
        trace(message, null);
    }

    @Override
    public void debug(String message) {
        debug(message, null);
    }

    @Override
    public void warn(String message) {
        warn(message, null);
    }

    @Override
    public void info(String message) {
        info(message, null);
    }

    @Override
    public void error(String message) {
        error(message, null);
    }

    @Override
    public void trace(String message, Object[] arguments) {
        entries.add(new Entry("trace", message, arguments));
    }

    @Override
    public void debug(String message, Object[] arguments) {
        entries.add(new Entry("debug", message, arguments));
    }

    @Override
    public void warn(String message, Object[] arguments) {
        entries.add(new Entry("warn", message, arguments));
    }

    @Override
    public void info(String message, Object[] arguments) {
        entries.add(new Entry("info", message, arguments));
    }

    @Override
    public void error(String message, Object[] arguments) {
        entries.add(new Entry("error", message, arguments));
    }
}
