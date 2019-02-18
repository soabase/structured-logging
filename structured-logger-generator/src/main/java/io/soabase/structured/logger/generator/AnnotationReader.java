package io.soabase.structured.logger.generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final String annotationFullName;

    AnnotationReader(ProcessingEnvironment processingEnv, Element element, String annotationFullName, String annotationName) {
        this.processingEnv = processingEnv;
        this.annotationFullName = annotationFullName;
        Optional<? extends AnnotationMirror> annotation = (element == null) ? Optional.empty() :
                element.getAnnotationMirrors().stream()
                        .filter(mirror -> mirror.getAnnotationType().asElement().getSimpleName().toString().equals(annotationName))
                        .findFirst();
        if (!annotation.isPresent()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Internal error. Could not find annotation: " + annotationName, element);
        }
        this.annotationName = annotationName;
        values = new HashMap<>();
        if (annotation.isPresent()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> specifiedValues = annotation.get().getElementValues();
            Map<? extends ExecutableElement, ? extends AnnotationValue> valuesWithDefaults = processingEnv.getElementUtils().getElementValuesWithDefaults(annotation.get());
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

    public boolean getBoolean(String named)
    {
        Object value = values.get(named);
        //noinspection SimplifiableIfStatement
        if ( value != null )
        {
            return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : Boolean.valueOf(String.valueOf(value));
        }
        return false;
    }

    public String getString(String named) {
        Object value = values.getOrDefault(named, "");
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    public List<TypeMirror> getClasses(String named) {
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
}
