/**
 * Copyright 2019 Jordan Zimmerman
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.structured.logger.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import io.soabase.structured.logger.annotations.LoggerSchema;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({
        "io.soabase.structured.logger.annotations.LoggerSchema",
        "io.soabase.structured.logger.annotations.LoggerSchemas",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class StructuredLoggerSchemaGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        annotations.forEach(annotation -> {
            Set<? extends Element> elementsAnnotatedWith = environment.getElementsAnnotatedWith(annotation);
            elementsAnnotatedWith.forEach(element -> {
                if (element.getKind() == ElementKind.CLASS) {
                    TypeElement typeElement = (TypeElement) element;
                    processElement(typeElement, annotation);
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only classes can have the LoggerSchema annotation", element);
                }
            });
        });
        return true;
    }

    private void processElement(TypeElement element, TypeElement annotation) {
        if (annotation.getQualifiedName().toString().equals(LoggerSchema.class.getName())) {
            AnnotationReader annotationReader = new AnnotationReader(processingEnv, element, annotation.getSimpleName().toString());
            processSchemaElement(annotationReader, element);
        } else {
            processSchemaContainerElement(element, annotation);
        }
    }

    private void processSchemaContainerElement(TypeElement element, TypeElement annotation) {
        AnnotationReader annotationReader = new AnnotationReader(processingEnv, element, annotation.getSimpleName().toString());
        List<AnnotationMirror> classes = annotationReader.getAnnotations("value");
        classes.forEach(e -> {
            AnnotationReader subAnnotationReader = new AnnotationReader(processingEnv, e, e.getElementValues(), annotation.getSimpleName().toString());
            processSchemaElement(subAnnotationReader, element);
        });
    }

    private void processSchemaElement(AnnotationReader annotationReader, TypeElement element) {
        if (annotationReader.getAnnotationName() == null) {
            return;
        }

        List<TypeMirror> components = annotationReader.getClasses("value");
        String schemaName = annotationReader.getString("schemaName");
        String packageName = annotationReader.getString("packageName");
        boolean reuseExistingSchema = annotationReader.getBoolean("reuseExistingSchema");

        String schemaClassName = String.format(schemaName, element.getSimpleName());
        if (packageName.isEmpty()) {
            packageName = getPackage(element);
        }

        ClassName className = ClassName.get(packageName, schemaClassName);
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        for (TypeMirror parentInterface : components) {
            ClassName typeName = (ClassName) ClassName.get(parentInterface);
            builder.addSuperinterface(typeName);
            copyMethods(builder, className, parentInterface);
        }

        TypeSpec classSpec = builder.build();
        JavaFile javaFile = JavaFile.builder(packageName, classSpec)
                .addFileComment("Auto generated from annotation on $L by Soabase $L annotation processor", element.getQualifiedName(), LoggerSchema.class.getSimpleName())
                .indent("    ")
                .build();

        Filer filer = processingEnv.getFiler();
        try {
            JavaFileObject sourceFile = filer.createSourceFile(className.toString());
            try (Writer writer = sourceFile.openWriter()) {
                javaFile.writeTo(writer);
            }
        } catch (IOException e) {
            if (!(e instanceof FilerException) || !reuseExistingSchema) {
                String message = "Could not create source file";
                if (e.getMessage() != null) {
                    message = message + ": " + e.getMessage();
                }
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
            }
        }
    }

    private void copyMethods(TypeSpec.Builder builder, ClassName className, TypeMirror parentInterface) {
        TypeElement parentInterfaceElement = (TypeElement) processingEnv.getTypeUtils().asElement(parentInterface);
        for (Element member : processingEnv.getElementUtils().getAllMembers(parentInterfaceElement)) {
            if (member.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) member;
                if (parentInterfaceElement.equals(method.getEnclosingElement())) {
                    String methodName = method.getSimpleName().toString();
                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .returns(className);

                    for (VariableElement parameter : method.getParameters()) {
                        methodBuilder.addParameter(ParameterSpec.get(parameter));
                    }

                    for (AnnotationMirror annotation : processingEnv.getElementUtils().getAllAnnotationMirrors(method)) {
                        methodBuilder.addAnnotation(AnnotationSpec.get(annotation));
                    }

                    builder.addMethod(methodBuilder.build());
                }
            }
        }
    }

    private String getPackage(TypeElement element) {
        while (element.getNestingKind().isNested()) {
            Element enclosingElement = element.getEnclosingElement();
            if (enclosingElement instanceof TypeElement) {
                element = (TypeElement) enclosingElement;
            } else {
                break;
            }
        }
        return element.getEnclosingElement().toString();
    }
}
