package io.soabase.structured.logger.generation;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class SchemaNames {
    final List<String> names;
    final Set<Integer> requireds;

    SchemaNames(List<String> names, Set<Integer> requireds) {
        this.names = Collections.unmodifiableList(names);
        this.requireds = Collections.unmodifiableSet(requireds);
    }
}
