package com.queryflow.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateFormat {

    String parse();

    String format() default "yyyyMMddHHmmss";

}
