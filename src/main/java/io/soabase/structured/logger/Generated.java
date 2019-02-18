package io.soabase.structured.logger;

import java.util.Map;
import java.util.function.BiConsumer;

public interface Generated<T> {
    Class<T> generated();

    void apply(String mainMessage, Map<String, Object> values, Throwable t, BiConsumer<String, Object[]> consumer);
}
