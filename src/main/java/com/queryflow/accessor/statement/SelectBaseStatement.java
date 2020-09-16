package com.queryflow.accessor.statement;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.accessor.interceptor.StatementInterceptors;
import com.queryflow.common.FetchDirection;
import com.queryflow.common.ResultMap;
import com.queryflow.common.ResultSetConcurType;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class SelectBaseStatement<T extends SelectBaseStatement> extends BaseStatement<T> {

    SelectBaseStatement(String sql, ConnectionExecutor executor) {
        super(sql, executor);
    }

    public T concurrentUpdatable() {
        statementInitConfig.resultSetConcurType = ResultSetConcurType.UPDATABLE;
        return (T) this;
    }

    public T setMaxFieldSize(int max) {
        interceptors.registerInterceptor(StatementInterceptors.maxFieldSize(max));
        return (T) this;
    }

    public T setFetchSize(int rows) {
        interceptors.registerInterceptor(StatementInterceptors.fetchSize(rows));
        return (T) this;
    }

    public T setFetchDirection(FetchDirection direction) {
        interceptors.registerInterceptor(StatementInterceptors.fetchDirection(direction));
        return (T) this;
    }

    public T setMaxRows(int rows) {
        interceptors.registerInterceptor(StatementInterceptors.maxRows(rows));
        return (T) this;
    }

    public T fillDicName() {
        return (T) this;
    }

    public abstract <R> R one(Class<R> type);

    public abstract ResultMap oneMap();

    public abstract <R> List<R> list(Class<R> type);

    public abstract List<ResultMap> listMap();

    public abstract <R> R result(ResultSetHandler<R> handler);

}
