package com.queryflow.mapper.executor;

import com.queryflow.mapper.SqlValue;

import java.util.List;

public class ExecuteMethodExecutor extends MapperMethodExecutor {

    public ExecuteMethodExecutor(String dataSourceTag, String sql, List<SqlValue> sqlValues) {
        super(dataSourceTag, sql, sqlValues);
    }

    @Override
    public Object execute(Object target, Object[] args) {
        Object[] values = fillValues(args);
        getAccessor().update(sql, values);
        return null;
    }

}
