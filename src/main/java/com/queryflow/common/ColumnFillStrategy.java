package com.queryflow.common;

/**
 * 字段自动填充策略
 *
 * @since 1.2.0
 * @author Jon
 */
public interface ColumnFillStrategy {

    /**
     * 返回要填充到字段的值。默认使用 {@link DefaultColumnFillStrategy}，
     * 如果原值不为 {@code null}，则直接返回；如果字段类型为 {@link java.util.Date} 或其子类，则返回当前的日期、时间；
     * 如果字段类型为 {@code String}，且指定的 {@code fillDatePattern} 不为空，则填充指定格式的当前时间格式化字符串。
     *
     * @param columnClass 字段的类型
     * @param value 字段原值
     * @param pattern 指定的日期格式字符串（{@code fillDatePattern}）
     * @param operation 当前正在执行的操作，插入或更新，{@link Operation}
     * @return 返回最终要填充到字段的值
     * @see DefaultColumnFillStrategy
     */
    Object fill(Class<?> columnClass, Object value, String pattern, Operation operation);

}
