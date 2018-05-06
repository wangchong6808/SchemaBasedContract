package com.jsonschema.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target(METHOD)
public @interface JsonSchema {

    String ns() default "";

    String inputSchema() default "";

    String outputSchema() default "";

}
