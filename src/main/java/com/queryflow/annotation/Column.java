package com.queryflow.annotation;

import com.queryflow.common.ColumnFillStrategy;
import com.queryflow.common.FillType;
import com.queryflow.common.DefaultColumnFillStrategy;
import com.queryflow.common.DictionaryEnum;
import com.queryflow.common.type.DefaultTypeHandler;
import com.queryflow.common.type.TypeHandler;

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
     * @return DictionaryEnum 实现类
     */
    Class<? extends DictionaryEnum> dictionaryClass() default DictionaryEnum.class;

    /**
     * 指定字段的填充类型，即不再需要每次手动为字段赋值
     *
     * @return {code FillType} 字段的填充类型
     * @see FillType
     * @since 1.2.0
     */
    FillType fillType() default FillType.NONE;

    /**
     * 字段的自动填充策略
     *
     * @return {@code ColumnFillStrategy} 的实现类
     * @see ColumnFillStrategy
     * @see DefaultColumnFillStrategy
     * @since 1.2.0
     */
    Class<? extends ColumnFillStrategy> fillStrategy() default DefaultColumnFillStrategy.class;

    /**
     * 如果指定了 {@code fillType}（非 {@code FillType.NONE}），并且使用默认的填充策略({@code DefaultColumnFillStrategy})，
     * 同时字段的为字符串，
     *
     * @return 格式字符串
     * @since 1.2.0
     */
    String fillDatePattern() default DefaultColumnFillStrategy.DEFAULT_FILL_PATTERN;

    /**
     * 更新数据时的组别，无需在更新时一个一个指定列名称，只需在注解上设置响应的 {@code Class}，即可自动更新这些字段
     *
     * @return 任意 {@code Class} 实例
     */
    Class<?>[] updateGroups() default {};

    Class<? extends TypeHandler<?>> typeHandler() default DefaultTypeHandler.class;

}
