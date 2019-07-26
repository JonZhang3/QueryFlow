package com.queryflow.accessor.runner;

import com.queryflow.accessor.interceptor.InterceptorHelper;
import com.queryflow.accessor.interceptor.Interceptors;
import com.queryflow.config.GlobalConfig;
import com.queryflow.common.DataType;
import com.queryflow.utils.Utils;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSqlRunner implements SqlRunner {

    private volatile boolean canUsePmd = true;

    protected Statement statement(Connection conn, Interceptors interceptors) throws SQLException {
        return InterceptorHelper.statement(conn, interceptors);
    }

    protected PreparedStatement prepareStatement(Connection conn, String sql,
                                                 String[] keyColumnNames,
                                                 Interceptors interceptors, boolean returnKeys) throws SQLException {

        return InterceptorHelper.prepareStatement(conn, sql, keyColumnNames, interceptors, returnKeys);
    }

    protected CallableStatement prepareCall(Connection conn, String sql, Interceptors interceptors) throws SQLException {
        return InterceptorHelper.prepareCall(conn, sql, interceptors);
    }

    protected void fillStatement(PreparedStatement statement, List<Object> params) throws SQLException {
        ParameterMetaData metaData = null;
        int paramsCount = params == null ? 0 : params.size();
        if (canUsePmd) {
            try {
                metaData = statement.getParameterMetaData();
                if (metaData != null) {
                    int stmtCount = metaData.getParameterCount();
                    if (stmtCount != paramsCount) {
                        throw new SQLException("Wrong number of parameters: expected "
                            + stmtCount + ", was given " + paramsCount);
                    }
                } else {
                    canUsePmd = false;
                }
            } catch (SQLFeatureNotSupportedException e) {
                canUsePmd = false;
            }
        }

        if (paramsCount == 0) {
            return;
        }
        CallableStatement call = null;
        if (statement instanceof CallableStatement) {
            call = (CallableStatement) statement;
        }

        for (int i = 0, len = params.size(); i < len; i++) {
            Object param = params.get(i);
            if (param != null) {
                if (call != null && param instanceof OutParameter) {
                    ((OutParameter) param).setTo(call, i + 1);
                } else {
                    statement.setObject(i + 1, param);
                }
            } else {
                int sqlType = DataType.VARCHAR.value();
                if (canUsePmd) {
                    try {
                        sqlType = metaData.getParameterType(i + 1);
                    } catch (SQLException e) {
                        canUsePmd = false;
                    }
                }
                statement.setNull(i + 1, sqlType);
            }
        }
    }

    protected String buildErrorMessage(SQLException e, String sql, List<Object> params) {
        String message = e.getMessage();
        if(Utils.isEmpty(message)) {
            message = "";
        }
        StringBuilder msg = new StringBuilder(message);
        msg.append(", sql:[");
        msg.append(sql);
        msg.append("] params:");
        if(params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params.toArray()));
        }
        return msg.toString();
    }

}
