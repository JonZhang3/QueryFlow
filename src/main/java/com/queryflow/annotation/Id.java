package com.queryflow.annotation;

import com.queryflow.key.KeyGenerator;
import com.queryflow.key.SnowflakeKeyGenerator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {

    /**
     * 指定 ID 字段名称，如果你不指定该值（null 或 空字符串），将使用 Java代码中的属性名作为
     * 该字段在数据库中的名称
     *
     * @return 列名称
     */
    String value() default "";

    /**
     * 为 ID 字段指定生成策略。默认生成策略为 {@code SnowflakeKeyGenerator}
     *
     * @return ID 生成实现的 Class
     * @see SnowflakeKeyGenerator
     */
    Class<? extends KeyGenerator> keyGenerator() default SnowflakeKeyGenerator.class;

}
