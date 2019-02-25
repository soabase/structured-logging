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
