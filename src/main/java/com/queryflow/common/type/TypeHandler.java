package com.queryflow.common.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    /**
     * 向 {@code Statement} 中设置数据
     *
     * @param value 实体类中字段的值
     * @return 要设置到 {@code Statement} 中的最终值
     */
    Object setToStatement(T value);

    /**
     * 从 {@code ResultSet} 中获取数据
     *
     * @param rs {@link ResultSet}
     * @param columnIndex 列所在的位置
     * @param valueClass 实体类中对应字段的类型
     * @return 要设置到实体类中字段的最终值
     */
    T getValue(ResultSet rs, int columnIndex, Class<?> valueClass) throws SQLException;

}
