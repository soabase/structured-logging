package io.soabase.structured.logger.generation;

import java.util.function.BiConsumer;

public interface Generated<T> {
    T newInstance(boolean hasException);

    void apply(T instance, String mainMessage, Throwable t, BiConsumer<String, Object[]> consumer);
}
