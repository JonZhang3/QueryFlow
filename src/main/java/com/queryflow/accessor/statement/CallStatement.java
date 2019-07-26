package com.queryflow.accessor.statement;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.connection.ConnectionExecutor;
import com.queryflow.accessor.handler.BeanListResultSetHandler;
import com.queryflow.accessor.handler.BeanResultSetHandler;
import com.queryflow.accessor.handler.MapListResultSetHandler;
import com.queryflow.accessor.handler.MapResultSetHandler;
import com.queryflow.accessor.runner.OutParameter;
import com.queryflow.common.ResultMap;
import com.queryflow.common.DataType;

import java.util.List;

public class CallStatement extends SelectBaseStatement<CallStatement> {

    private final OutParameters parameters = new OutParameters();

    public CallStatement(String sql, ConnectionExecutor executor) {
        super(sql, executor);
    }

    public CallStatement registerOutParameter(String name, DataType dataType) {
        if (dataType != null) {
            OutParameter outParameter = new OutParameter(dataType);
            parameters.putParameter(name, outParameter);
            params.add(outParameter);
        }
        return this;
    }

    public CallStatement registerOutParameter(String name, OutParameter outParameter) {
        if (outParameter != null) {
            parameters.putParameter(name, outParameter);
            params.add(outParameter);
        }
        return this;
    }

    public OutParameters execute() {
        internalExecute();
        parameters.updateRows = executor.call(sql, params, interceptors);
        return parameters;
    }

    public <T> T one(Class<T> type) {
        internalExecute();
        return (T) executor.call(sql, params, interceptors, BeanResultSetHandler.newBeanHandler(type));
    }

    public ResultMap oneMap() {
        internalExecute();
        return executor.call(sql, params, interceptors, new MapResultSetHandler());
    }

    public <T> List<T> list(Class<T> type) {
        internalExecute();
        return executor.call(sql, params, interceptors, new BeanListResultSetHandler<>(type));
    }

    public List<ResultMap> listMap() {
        internalExecute();
        return executor.call(sql, params, interceptors, new MapListResultSetHandler());
    }

    public <T> T result(ResultSetHandler<T> handler) {
        internalExecute();
        return executor.call(sql, params, interceptors, handler);
    }

}
