package com.queryflow.common.type;

import java.sql.ResultSet;

public class DefaultTypeHandler implements TypeHandler<Object> {

    @Override
    public Object setToStatement(Object value) {
        return value;
    }

    @Override
    public Object getValue(ResultSet rs, int index, Class<?> valueClass) {
        return null;
    }
}
