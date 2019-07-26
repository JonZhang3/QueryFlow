package com.queryflow.annotation;

import java.lang.annotation.*;

/**
 * 标识类是一个 Mapper，从而识别出类内方法的注解 SQL
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mapper {

}
