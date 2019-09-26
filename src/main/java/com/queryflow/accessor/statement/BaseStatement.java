package com.queryflow.accessor.statement;

import com.queryflow.accessor.connection.ConnectionExecutor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseStatement<T extends BaseStatement> extends Statement<T> {

    protected List<Object> params = new LinkedList<>();
    protected String sql;

    BaseStatement(String sql, ConnectionExecutor executor) {
        super(executor);
        this.sql = sql;
    }

    public T bind(Object value) {
        params.add(value);
        return (T) this;
    }

    public T bindArray(Object[] values) {
        if(values != null && values.length > 0) {
            Collections.addAll(params, values);
        }
        return (T) this;
    }

    public T bindList(List<Object> values) {
        if(values != null && !values.isEmpty()) {
            params.addAll(values);
        }
        return (T) this;
    }

    protected void internalExecute() {
        interceptors.setStatementInitConfig(statementInitConfig);
    }

}
