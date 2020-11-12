package com.queryflow.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    /**
     * 指定方法中要使用到的数据源。
     * 如果与 {@code @Transactional} 一起使用时，可配置多个数据源。
     * 如果与 {@code @Mapper} 一起使用时，只能配置一个数据源，如果配置了多个，则取第一个数据源
     *
     * @return 数据源标识
     * @since 1.2.0
     */
    String[] value() default {};

    /**
     * 当使用事务 {@code @Transactional} 时，并且具有多数据源，指定需要排除的数据源
     *
     * @return 数据源标识
     * @since 1.2.0
     */
    String[] excluded() default {};

}
