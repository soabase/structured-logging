package io.soabase.structured.logger.formatting;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class LevelLoggers {
    public static final LevelLogger trace = new LevelLogger() {
        @Override
        public boolean isEnabled(Logger logger) {
            return logger.isTraceEnabled();
        }

        @Override
        public void log(Logger logger, String msg) {
            logger.trace(msg);
        }

        @Override
        public void log(Logger logger, String format, Object arg) {
            logger.trace(format, arg);
        }

        @Override
        public void log(Logger logger, String format, Object arg1, Object arg2) {
            logger.trace(format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, String format, Object... arguments) {
            logger.trace(format, arguments);
        }

        @Override
        public void log(Logger logger, String msg, Throwable t) {
            logger.trace(msg, t);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg) {
            logger.trace(marker, msg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg) {
            logger.trace(marker, format, arg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
            logger.trace(marker, format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... argArray) {
            logger.trace(marker, format, argArray);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg, Throwable t) {
            logger.trace(marker, msg, t);
        }
    };

    public static final LevelLogger debug = new LevelLogger() {
        @Override
        public boolean isEnabled(Logger logger) {
            return logger.isDebugEnabled();
        }

        @Override
        public void log(Logger logger, String msg) {
            logger.debug(msg);
        }

        @Override
        public void log(Logger logger, String format, Object arg) {
            logger.debug(format, arg);
        }

        @Override
        public void log(Logger logger, String format, Object arg1, Object arg2) {
            logger.debug(format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, String format, Object... arguments) {
            logger.debug(format, arguments);
        }

        @Override
        public void log(Logger logger, String msg, Throwable t) {
            logger.debug(msg, t);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg) {
            logger.debug(marker, msg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg) {
            logger.debug(marker, format, arg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
            logger.debug(marker, format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... argArray) {
            logger.debug(marker, format, argArray);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg, Throwable t) {
            logger.debug(marker, msg, t);
        }
    };

    public static final LevelLogger info = new LevelLogger() {
        @Override
        public boolean isEnabled(Logger logger) {
            return logger.isInfoEnabled();
        }

        @Override
        public void log(Logger logger, String msg) {
            logger.info(msg);
        }

        @Override
        public void log(Logger logger, String format, Object arg) {
            logger.info(format, arg);
        }

        @Override
        public void log(Logger logger, String format, Object arg1, Object arg2) {
            logger.info(format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, String format, Object... arguments) {
            logger.info(format, arguments);
        }

        @Override
        public void log(Logger logger, String msg, Throwable t) {
            logger.info(msg, t);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg) {
            logger.info(marker, msg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg) {
            logger.info(marker, format, arg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
            logger.info(marker, format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... argArray) {
            logger.info(marker, format, argArray);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg, Throwable t) {
            logger.info(marker, msg, t);
        }
    };

    public static final LevelLogger warn = new LevelLogger() {
        @Override
        public boolean isEnabled(Logger logger) {
            return logger.isWarnEnabled();
        }

        @Override
        public void log(Logger logger, String msg) {
            logger.warn(msg);
        }

        @Override
        public void log(Logger logger, String format, Object arg) {
            logger.warn(format, arg);
        }

        @Override
        public void log(Logger logger, String format, Object arg1, Object arg2) {
            logger.warn(format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, String format, Object... arguments) {
            logger.warn(format, arguments);
        }

        @Override
        public void log(Logger logger, String msg, Throwable t) {
            logger.warn(msg, t);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg) {
            logger.warn(marker, msg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg) {
            logger.warn(marker, format, arg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
            logger.warn(marker, format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... argArray) {
            logger.warn(marker, format, argArray);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg, Throwable t) {
            logger.warn(marker, msg, t);
        }
    };

    public static final LevelLogger error = new LevelLogger() {
        @Override
        public boolean isEnabled(Logger logger) {
            return logger.isErrorEnabled();
        }

        @Override
        public void log(Logger logger, String msg) {
            logger.error(msg);
        }

        @Override
        public void log(Logger logger, String format, Object arg) {
            logger.error(format, arg);
        }

        @Override
        public void log(Logger logger, String format, Object arg1, Object arg2) {
            logger.error(format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, String format, Object... arguments) {
            logger.error(format, arguments);
        }

        @Override
        public void log(Logger logger, String msg, Throwable t) {
            logger.error(msg, t);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg) {
            logger.error(marker, msg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg) {
            logger.error(marker, format, arg);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
            logger.error(marker, format, arg1, arg2);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... argArray) {
            logger.error(marker, format, argArray);
        }

        @Override
        public void log(Logger logger, Marker marker, String msg, Throwable t) {
            logger.error(marker, msg, t);
        }
    };

    private LevelLoggers() {
    }
}
