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

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestLoggerFacade implements Logger {
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
    public String getName() {
        return null;
    }

    @Override
    public void trace(String format, Object arg) {
        trace(format, new Object[]{arg});
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(String msg, Throwable t) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void debug(String format, Object arg) {
        debug(format, new Object[]{arg});
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String msg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void info(String format, Object arg) {
        info(format, new Object[]{arg});
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void warn(String format, Object arg) {
        warn(format, new Object[]{arg});
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void error(String format, Object arg) {
        error(format, new Object[]{arg});
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {

    }

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
        trace(message, new Object[0]);
    }

    @Override
    public void debug(String message) {
        debug(message, new Object[0]);
    }

    @Override
    public void warn(String message) {
        warn(message, new Object[0]);
    }

    @Override
    public void info(String message) {
        info(message, new Object[0]);
    }

    @Override
    public void error(String message) {
        error(message, new Object[0]);
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
