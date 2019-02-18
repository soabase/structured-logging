package io.soabase.structured.logger;

import java.util.List;

public interface Generated<T> {
    Class<T> generated();
    List<String> names();
    String formatString();
    boolean hasCustom();
    LoggingFormatter loggingFormatter();
}
