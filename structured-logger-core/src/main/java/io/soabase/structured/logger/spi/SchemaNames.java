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
package io.soabase.structured.logger.spi;

import io.soabase.structured.logger.StructuredLoggerFactory;
import io.soabase.structured.logger.annotations.Required;
import io.soabase.structured.logger.annotations.SortOrder;
import io.soabase.structured.logger.exception.InvalidSchemaException;
import io.soabase.structured.logger.exception.MissingSchemaValueException;
import io.soabase.structured.logger.formatting.Arguments;
import io.soabase.structured.logger.schemas.WithFormat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Utility class. Holds schema method names and required method names
 */
public class SchemaNames {
    private final List<String> names;
    private final List<String> formattedNames;
    private final Set<Integer> requireds;

    /**
     * Build a new SchemaNames instance for the given schema class.
     *
     * @param schemaClass schema class
     * @param reservedNames if you have reserved names that cannot be used, pass them here
     * @return schema names
     */
    public static SchemaNames build(Class<?> schemaClass, Collection<String> reservedNames) {
        return build(schemaClass, reservedNames, UnaryOperator.identity());
    }

    public static SchemaNames build(Class<?> schemaClass, Collection<String> reservedNames, UnaryOperator<String> nameFormatter) {
        if (!schemaClass.isInterface()) {
            throw new InvalidSchemaException("Schema must be an interface. Schema: " + schemaClass.getName());
        }

        Set<String> requiredNames = new HashSet<>();
        List<String> schemaNames = new ArrayList<>();
        Set<String> usedMethods = new HashSet<>();
        Map<String, Integer> schemaNameToSortOrder = new HashMap<>();
        for (Method method : schemaClass.getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }
            if (!method.getReturnType().isAssignableFrom(schemaClass)) {
                throw new InvalidSchemaException("Schema methods must return " + schemaClass.getSimpleName() + " or a subclass of it. Method: " + method.getName());
            }
            if (!usedMethods.add(method.getName())) {
                throw new InvalidSchemaException("Schema method names must be unique. Duplicate: " + method.getName());
            }
            if (!method.getDeclaringClass().equals(WithFormat.class)) {
                if (method.getParameterCount() != 1) {
                    throw new InvalidSchemaException("Schema methods must take exactly 1 argument. Method: " + method.getName());
                }
                if (reservedNames.contains(method.getName())) {
                    throw new InvalidSchemaException("Schema method name is reserved for internal use. Name: " + method.getName());
                }
            }
            if (method.getAnnotation(Required.class) != null) {
                requiredNames.add(method.getName());
            }
            schemaNames.add(method.getName());

            SortOrder sortOrder = method.getAnnotation(SortOrder.class);
            int sortOrderValue = (sortOrder != null) ? sortOrder.value() : Short.MAX_VALUE;
            schemaNameToSortOrder.put(method.getName(), sortOrderValue);
        }
        schemaNames.sort((name1, name2) -> compareSchemaNames(schemaNameToSortOrder, name1, name2));
        List<String> formattedNames = schemaNames.stream().map(nameFormatter).collect(Collectors.toList());
        Set<Integer> requireds = requiredNames.stream().map(schemaNames::indexOf).collect(Collectors.toSet());
        return new SchemaNames(schemaNames, formattedNames, requireds);
    }

    public SchemaNames(List<String> names, List<String> formattedNames, Set<Integer> requireds) {
        this.names = Collections.unmodifiableList(names);
        this.formattedNames = Collections.unmodifiableList(formattedNames);
        this.requireds = Collections.unmodifiableSet(requireds);
    }

    public List<String> getFormattedNames() {
        return formattedNames;
    }

    public List<String> getNames() {
        return names;
    }

    public Set<Integer> getRequireds() {
        return requireds;
    }

    /**
     * If required value checking is enabled, validate the given arguments to make sure required values are set
     *
     * @param arguments arguments
     */
    public void validateRequired(Arguments arguments) {
        if (StructuredLoggerFactory.requiredValuesEnabled() && !requireds.isEmpty()) {
            requireds.forEach(index -> {
                if (arguments.get(index) == null) {
                    throw new MissingSchemaValueException("Entire schema must be specified. Missing: " + names.get(index));
                }
            });
        }
    }

    private static int compareSchemaNames(Map<String, Integer> schemaNameToSortOrder, String name1, String name2) {
        int sortValue1 = schemaNameToSortOrder.get(name1);
        int sortValue2 = schemaNameToSortOrder.get(name2);
        int diff = sortValue1 - sortValue2;
        if (diff == 0) {
            diff = name1.compareTo(name2);
        }
        return diff;
    }
}
