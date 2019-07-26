package com.queryflow.accessor.statement;

import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.accessor.interceptor.Interceptors;
import com.queryflow.accessor.interceptor.StatementInterceptors;

public abstract class Statement<T extends Statement> {

    protected ConnectionExecutor executor;
    protected Interceptors interceptors;
    protected StatementInitConfig statementInitConfig;

    Statement(ConnectionExecutor executor) {
        this.executor = executor;
        this.interceptors = new Interceptors();
        this.statementInitConfig = new StatementInitConfig();
    }

    public T setQueryTimeout(int seconds) {
        interceptors.registerInterceptor(StatementInterceptors.queryTimeout(seconds));
        return (T) this;
    }

}
