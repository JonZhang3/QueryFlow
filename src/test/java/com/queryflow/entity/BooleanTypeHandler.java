package com.queryflow.entity;

import com.queryflow.common.type.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public Object setToStatement(Boolean value) {
        return null;
    }

    @Override
    public Boolean getValue(ResultSet rs, int columnIndex, Class<?> valueClass) throws SQLException {
        return null;
    }

}
