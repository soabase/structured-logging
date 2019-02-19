package io.soabase.structured.logger;

public interface LoggerFacade {
    boolean isTraceEnabled();

    boolean isDebugEnabled();

    boolean isWarnEnabled();

    boolean isInfoEnabled();

    boolean isErrorEnabled();

    void trace(String message, Object[] arguments);

    void debug(String message, Object[] arguments);

    void warn(String message, Object[] arguments);

    void info(String message, Object[] arguments);

    void error(String message, Object[] arguments);
}
