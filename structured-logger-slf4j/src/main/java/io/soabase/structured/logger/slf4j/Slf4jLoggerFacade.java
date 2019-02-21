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
package io.soabase.structured.logger.slf4j;

import io.soabase.structured.logger.LoggerFacade;
import org.slf4j.Logger;

import java.util.Objects;

public class Slf4jLoggerFacade implements LoggerFacade {
    private final Logger logger;

    public Slf4jLoggerFacade(Logger logger) {
        this.logger = Objects.requireNonNull(logger, "logger cannot be null");
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void trace(String message, Object[] arguments) {
        logger.trace(message, arguments);
    }

    @Override
    public void debug(String message, Object[] arguments) {
        logger.debug(message, arguments);
    }

    @Override
    public void warn(String message, Object[] arguments) {
        logger.warn(message, arguments);
    }

    @Override
    public void info(String message, Object[] arguments) {
        logger.info(message, arguments);
    }

    @Override
    public void error(String message, Object[] arguments) {
        logger.error(message, arguments);
    }
}
