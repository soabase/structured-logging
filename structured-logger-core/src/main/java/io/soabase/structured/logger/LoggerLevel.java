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
package io.soabase.structured.logger;

import org.slf4j.Logger;

public enum LoggerLevel {
    TRACE() {
        @Override
        public void log(Logger logger, String message) {
            logger.trace(message);
        }

        @Override
        public void log(Logger logger, String message, Object[] arguments) {
            logger.trace(message, arguments);
        }
    },

    DEBUG {
        @Override
        public void log(Logger logger, String message) {
            logger.debug(message);
        }

        @Override
        public void log(Logger logger, String message, Object[] arguments) {
            logger.debug(message, arguments);
        }
    },

    WARN {
        @Override
        public void log(Logger logger, String message) {
            logger.warn(message);
        }

        @Override
        public void log(Logger logger, String message, Object[] arguments) {
            logger.warn(message, arguments);
        }
    },

    INFO {
        @Override
        public void log(Logger logger, String message) {
            logger.info(message);
        }

        @Override
        public void log(Logger logger, String message, Object[] arguments) {
            logger.info(message, arguments);
        }
    },

    ERROR {
        @Override
        public void log(Logger logger, String message) {
            logger.error(message);
        }

        @Override
        public void log(Logger logger, String message, Object[] arguments) {
            logger.error(message, arguments);
        }
    }
    ;

    public abstract void log(Logger logger, String message);

    public abstract void log(Logger logger, String message, Object[] arguments);
}
