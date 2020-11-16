package com.queryflow.common.type;

import com.queryflow.utils.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultTypeHandler implements TypeHandler<Object> {

    @Override
    public Object setToStatement(Object value) {
        return value;
    }

    @Override
    public Object getValue(ResultSet rs, int columnIndex, Class<?> valueClass) throws SQLException {
        return JdbcUtil.getResultSetValue(rs, columnIndex, valueClass);
    }

    public Object getValue(ResultSet rs, String columnName, Class<?> valueClass) throws SQLException {
        return null;
    }

}
