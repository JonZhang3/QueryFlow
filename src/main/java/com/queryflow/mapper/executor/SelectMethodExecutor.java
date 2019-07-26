package com.queryflow.mapper.executor;

import com.queryflow.mapper.SqlValue;

import java.util.List;

public abstract class SelectMethodExecutor extends MapperMethodExecutor {

    protected Class<?> requiredType;

    public SelectMethodExecutor(String dataSourceTag, String sql,
                                List<SqlValue> sqlValues, Class<?> requiredType) {
        super(dataSourceTag, sql, sqlValues);
        this.requiredType = requiredType;
    }
}
