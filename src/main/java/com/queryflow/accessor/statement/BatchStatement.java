package com.queryflow.accessor.statement;

import com.queryflow.accessor.connection.ConnectionExecutor;

import java.util.LinkedList;
import java.util.List;

public class BatchStatement extends Statement<BatchStatement> {

    private List<String> sqls = new LinkedList<>();

    public BatchStatement(ConnectionExecutor executor) {
        super(executor);
    }

    public BatchStatement add(String sql) {
        this.sqls.add(sql);
        return this;
    }

    public int[] execute() {
        if(sqls.isEmpty()) {
            return new int[0];
        }
        interceptors.setStatementInitConfig(statementInitConfig);
        return executor.batchUpdate(sqls, interceptors);
    }

}
