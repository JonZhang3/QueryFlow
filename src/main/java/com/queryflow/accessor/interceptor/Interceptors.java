package com.queryflow.accessor.interceptor;

import com.queryflow.accessor.statement.StatementInitConfig;

import java.util.LinkedList;
import java.util.List;

public class Interceptors {

    private final List<Interceptor> interceptors = new LinkedList<>();
    private StatementInitConfig statementInitConfig;

    public void registerInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void setStatementInitConfig(StatementInitConfig initConfig) {
        this.statementInitConfig = initConfig;
    }

    public void unshift(Interceptors interceptors) {
        this.interceptors.addAll(0, interceptors.interceptors);
    }

    public void push(Interceptors interceptors) {
        this.interceptors.addAll(interceptors.interceptors);
    }

    public StatementInitConfig getStatementInitConfig() {
        return statementInitConfig;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

}
