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
package io.soabase.structured.logger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     Generates schema classes from "mixins" use Java Annotation Processing (see http://docs.oracle.com/javase/7/docs/technotes/guides/apt).
 *     The processor FQN is <code>io.soabase.structured.logger.generator.StructuredLoggerSchemaGenerator</code>.
 * </p>
 *
 * <p>
 *     Depending on your development environment, you may need to enable annotation processing. Here are links to how to do this for commonly used development tools:
 *
 *     <li>IntelliJ IDEA: https://www.jetbrains.com/help/idea/2016.1/configuring-annotation-processing.html</li>
 *     <li>Eclipse: https://www.eclipse.org/jdt/apt/introToAPT.php</li>
 *     <li>NetBeans: https://netbeans.org/kb/docs/java/annotations.html</li>
 *     <li>Maven: https://maven.apache.org/guides/mini/guide-generating-sources.html</li>
 *     <li>Gradle: http://blog.jdriven.com/2016/03/gradle-goodness-enable-compiler-annotation-processing-intellij-idea/</li>
 * </p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoggerSchema {
    /**
     * List of mixin classes that will form the super-interfaces of the schema
     *
     * @return list of classes
     */
    Class[] value();

    /**
     * The name of the generated class. If not provided, the class is the name of the class that is annotated
     * plus "Schema". Note: include "%s" in your schema name and it will be replaced with the annotated class name
     *
     * @return schema name
     */
    String schemaName() default "";

    /**
     * Package for generated class. If not provided, the annotated class's package is used
     *
     * @return package name
     */
    String packageName() default "";

    /**
     * If true, generated schema classes can be duplicated without error. This way, you can use the same
     * combination of mixins without concern for naming.
     *
     * @return true/false
     */
    boolean reuseExistingSchema() default true;
}
