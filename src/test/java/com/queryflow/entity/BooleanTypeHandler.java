package com.queryflow.entity;

import com.queryflow.common.type.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public Object setToStatement(Boolean value) {
        return value ? "1" : "0";
    }

    @Override
    public Boolean getValue(ResultSet rs, int columnIndex, Class<?> valueClass) throws SQLException {
        String value = rs.getString(columnIndex);
        return "1".equals(value) ? Boolean.TRUE : Boolean.FALSE;
    }

}
