package com.queryflow.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    /**
     * 指定方法中要使用到的数据源。
     *
     * @return 数据源标识
     */
    String value() default "";

}
