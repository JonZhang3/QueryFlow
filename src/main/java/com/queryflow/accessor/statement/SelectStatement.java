package com.queryflow.accessor.statement;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.common.ResultSetType;
import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.common.ResultMap;

import java.util.List;

public class SelectStatement extends SelectBaseStatement<SelectStatement> {

    public SelectStatement(String sql, ConnectionExecutor executor) {
        super(sql, executor);
    }

    public SelectStatement setResultSetType(ResultSetType resultSetType) {
        statementInitConfig.resultSetType = resultSetType;
        return this;
    }

    public <T> T one(Class<T> type) {
        internalExecute();
        return executor.queryForBean(sql, params, interceptors, type);
    }

    public ResultMap oneMap() {
        internalExecute();
        return executor.queryForMap(sql, params, interceptors);
    }

    public <T> List<T> list(Class<T> type) {
        internalExecute();
        return executor.queryForListBean(sql, params, interceptors, type);
    }

    public List<ResultMap> listMap() {
        internalExecute();
        return executor.queryForListMap(sql, params, interceptors);
    }

    public <T> T result(ResultSetHandler<T> handler) {
        internalExecute();
        return executor.query(sql, params, interceptors, handler);
    }

}
