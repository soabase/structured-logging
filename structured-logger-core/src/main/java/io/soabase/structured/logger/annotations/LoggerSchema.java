package io.soabase.structured.logger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoggerSchema {
    Class[] value() default {};

    String schemaFormatString() default "%sSchema";

    boolean schemaClassesExtendBase() default true;
}
