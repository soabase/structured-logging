package io.soabase.structured.logger;

public enum LoggerLevel {
    TRACE() {
        @Override
        public void log(LoggerFacade logger, String message) {
            logger.trace(message);
        }

        @Override
        public void log(LoggerFacade logger, String message, Object[] arguments) {
            logger.trace(message, arguments);
        }
    },

    DEBUG {
        @Override
        public void log(LoggerFacade logger, String message) {
            logger.debug(message);
        }

        @Override
        public void log(LoggerFacade logger, String message, Object[] arguments) {
            logger.debug(message, arguments);
        }
    },

    WARN {
        @Override
        public void log(LoggerFacade logger, String message) {
            logger.warn(message);
        }

        @Override
        public void log(LoggerFacade logger, String message, Object[] arguments) {
            logger.warn(message, arguments);
        }
    },

    INFO {
        @Override
        public void log(LoggerFacade logger, String message) {
            logger.info(message);
        }

        @Override
        public void log(LoggerFacade logger, String message, Object[] arguments) {
            logger.info(message, arguments);
        }
    },

    ERROR {
        @Override
        public void log(LoggerFacade logger, String message) {
            logger.error(message);
        }

        @Override
        public void log(LoggerFacade logger, String message, Object[] arguments) {
            logger.error(message, arguments);
        }
    }
    ;

    public abstract void log(LoggerFacade logger, String message);

    public abstract void log(LoggerFacade logger, String message, Object[] arguments);
}
