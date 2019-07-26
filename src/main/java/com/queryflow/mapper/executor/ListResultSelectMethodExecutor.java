package com.queryflow.mapper.executor;

import com.queryflow.mapper.SqlValue;

import java.util.List;

public class ListResultSelectMethodExecutor extends SelectMethodExecutor {
    public ListResultSelectMethodExecutor(String dataSourceTag, String sql, List<SqlValue> sqlValues, Class<?> requiredType) {
        super(dataSourceTag, sql, sqlValues, requiredType);
    }

    @Override
    public Object execute(Object target, Object[] args) {
        Object[] values = fillValues(args);
        return getAccessor().query(sql, values).list(requiredType);
    }
}
