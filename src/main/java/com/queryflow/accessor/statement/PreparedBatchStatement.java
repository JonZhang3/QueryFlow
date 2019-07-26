package com.queryflow.accessor.statement;

import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.common.ResultMap;

import java.util.LinkedList;
import java.util.List;

public class PreparedBatchStatement extends BaseStatement<PreparedBatchStatement> {

    private final List<List<Object>> values = new LinkedList<>();

    public PreparedBatchStatement(String sql, ConnectionExecutor executor) {
        super(sql, executor);
    }

    public PreparedBatchStatement add() {
        values.add(params);
        params.clear();
        return this;
    }

    public PreparedBatchStatement add(List<Object> params) {
        for (Object param : params) {
            bind(param);
        }
        add();
        return this;
    }

    public PreparedBatchStatement add(Object... params) {
        if(params != null) {
            for (Object param : params) {
                bind(param);
            }
            add();
        }
        return this;
    }

    public int size() {
        return values.size();
    }

    public int[] execute() {
        internalExecute();
        return executor.batchUpdate(sql, values, interceptors);
    }

    public List<ResultMap> executeAndReturnGeneratedKeys(String... columnNames) {
        internalExecute();
        return executor.batchInsertGetKes(sql, values, columnNames, interceptors, new KeysResultSetHandler(columnNames));
    }

}
