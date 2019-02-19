package io.soabase.structured.logger.generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnotationReader {
    private final String annotationName;
    private final Map<String, Object> values;
    private final ProcessingEnvironment processingEnv;

    AnnotationReader(ProcessingEnvironment processingEnv, Element element, String annotationName) {
        this.processingEnv = processingEnv;
        Optional<? extends AnnotationMirror> annotation = (element == null) ? Optional.empty() :
                processingEnv.getElementUtils().getAllAnnotationMirrors(element).stream()
                        .filter(mirror -> mirror.getAnnotationType().asElement().getSimpleName().toString().equals(annotationName))
                        .findFirst();
        if (annotation.isPresent()) {
            this.annotationName = annotationName;
            values = new HashMap<>();
            applyAnnotationWithDefaults(processingEnv, annotationName, annotation.get(), annotation.get().getElementValues());
        } else {
            this.annotationName = null;
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Internal error. Could not find annotation: " + annotationName, element);
            values = Collections.emptyMap();
        }
    }

    AnnotationReader(ProcessingEnvironment processingEnv, AnnotationMirror annotation, Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues, String annotationName) {
        this.annotationName = annotationName;
        this.processingEnv = processingEnv;
        values = new HashMap<>();
        applyAnnotationWithDefaults(processingEnv, annotationName, annotation, elementValues);
    }

    String getAnnotationName() {
        return annotationName;
    }

    boolean getBoolean(String named)
    {
        Object value = values.get(named);
        //noinspection SimplifiableIfStatement
        if ( value != null )
        {
            return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : Boolean.valueOf(String.valueOf(value));
        }
        return false;
    }

    String getString(String named) {
        Object value = values.getOrDefault(named, "");
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    List<AnnotationMirror> getAnnotations(String named) {
        Object value = values.get(named);
        if (value != null) {
            if (value instanceof Map) {
                value = ((Map)value).get(named);
            }
            if (value instanceof List) {
                return (List<AnnotationMirror>) value;
            }

            return Arrays.asList((AnnotationMirror[])value);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    List<TypeMirror> getClasses(String named) {
        Object value = values.get(named);
        if (value != null) {
            if (value instanceof List) {
                List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) value;
                return values.stream().map(v -> (TypeMirror) v.getValue()).collect(Collectors.toList());
            }

            return Arrays.stream(String.valueOf(value).split(","))
                    .filter(s -> s.trim().length() > 0)
                    .map(s -> processingEnv.getElementUtils().getTypeElement(s.trim()))
                    .filter(Objects::nonNull)
                    .map(Element::asType)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Object unwrapValue(Object value) {
        if (value instanceof AnnotationValue) {
            value = ((AnnotationValue)value).getValue();
        }
        return value;
    }

    private void applyAnnotationWithDefaults(ProcessingEnvironment processingEnv, String annotationName, AnnotationMirror annotation, Map<? extends ExecutableElement, ? extends AnnotationValue> specifiedValues) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valuesWithDefaults = processingEnv.getElementUtils().getElementValuesWithDefaults(annotation);
        valuesWithDefaults.forEach((key, value) -> {
            String overrideKey = annotationName + "." + key.getSimpleName().toString();
            if (specifiedValues.containsKey(key)) {
                values.put(key.getSimpleName().toString(), specifiedValues.get(key).getValue());
            } else if (processingEnv.getOptions().containsKey(overrideKey)) {
                values.put(key.getSimpleName().toString(), processingEnv.getOptions().get(overrideKey));
            } else {
                values.put(key.getSimpleName().toString(), value.getValue());
            }
        });
    }
}
