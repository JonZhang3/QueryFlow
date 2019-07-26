package com.queryflow.mapper.executor;

import com.queryflow.mapper.SqlValue;

import java.util.List;

public class OneResultSelectMethodExecutor extends SelectMethodExecutor {

    public OneResultSelectMethodExecutor(String dataSourceTag, String sql, List<SqlValue> sqlValues, Class<?> requiredType) {
        super(dataSourceTag, sql, sqlValues, requiredType);
    }

    @Override
    public Object execute(Object target, Object[] args) {
        Object[] values = fillValues(args);
        return getAccessor().query(sql, values).one(requiredType);
    }

}
