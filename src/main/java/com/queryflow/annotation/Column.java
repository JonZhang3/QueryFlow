package com.queryflow.annotation;

import com.queryflow.common.DictionaryEnum;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     * 数据库表中字段名称
     */
    String value() default "";

    /**
     * 指定该字段是否在表中存在
     */
    boolean exists() default true;

    /**
     * 指定该字段是否是一个数据字典字段
     */
    boolean isDictionaryKey() default false;

    /**
     * 如果该字段是一个数据字典字段，指定数据字典的枚举类
     */
    Class<? extends DictionaryEnum> dictionaryClass() default DictionaryEnum.class;

}
