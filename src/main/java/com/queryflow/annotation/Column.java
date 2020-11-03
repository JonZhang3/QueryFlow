package com.queryflow.annotation;

import com.queryflow.common.ColumnFillStrategy;
import com.queryflow.common.FillType;
import com.queryflow.common.DefaultColumnFillStrategy;
import com.queryflow.common.DictionaryEnum;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     * 数据库表中字段名称
     *
     * @return 列名
     */
    String value() default "";

    /**
     * 指定该字段是否在表中存在
     *
     * @return {@code true} 表示存在。否则表示不存在
     */
    boolean exists() default true;

    /**
     * 指定该字段是否是一个数据字典字段
     *
     * @return {@code true} 表示是
     */
    boolean isDictionaryKey() default false;

    /**
     * 如果该字段是一个数据字典字段，指定数据字典的枚举类
     *
     * @return DictionaryEnum 实现类的 Class
     */
    Class<? extends DictionaryEnum> dictionaryClass() default DictionaryEnum.class;

    FillType fillType() default FillType.NONE;

    Class<? extends ColumnFillStrategy> fillStrategy() default DefaultColumnFillStrategy.class;

}
