package com.queryflow.accessor.statement;

import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.common.ResultMap;

import java.util.List;

public class UpdateStatement extends BaseStatement<UpdateStatement> {

    public UpdateStatement(String sql, ConnectionExecutor executor) {
        super(sql, executor);
    }

    public int execute() {
        internalExecute();
        return executor.update(sql, params, interceptors);
    }

    public List<ResultMap> executeAndReturnGeneratedKeys(String... columnNames) {
        internalExecute();
        return executor.insertGetKey(sql, params, columnNames, interceptors, new KeysResultSetHandler(columnNames));
    }

}
