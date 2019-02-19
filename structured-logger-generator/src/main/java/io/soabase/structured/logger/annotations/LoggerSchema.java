package io.soabase.structured.logger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoggerSchema {
    Class[] value();

    String schemaFormatString() default "%sSchema";

    String schemaName() default "";

    String packageName() default "";

    boolean schemaClassesExtendBase() default true;

    boolean reuseExistingSchema() default true;
}
