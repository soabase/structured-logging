package io.soabase.structured.logger;

import java.util.HashMap;
import java.util.Map;

public class Instance {
    private final Map<String, Object> values = new HashMap<>();

    public void slogSetValue(String key, Object value) {
        values.put(key, value);
    }

    public Map<String, Object> slogGetValues() {
        return values;
    }
}
