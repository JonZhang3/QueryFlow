package com.queryflow.accessor.interceptor;

import com.queryflow.accessor.statement.StatementInitConfig;

import java.sql.*;

public final class InterceptorHelper {

    private InterceptorHelper() {
    }

    public static Statement statement(Connection conn, Interceptors interceptors) throws SQLException {
        Statement statement;
        StatementInitConfig config = null;
        if (interceptors != null) {
            config = interceptors.getStatementInitConfig();
        }
        if (config != null && config.resultSetType != null && config.resultSetConcurType != null) {
            statement = conn.createStatement(config.resultSetType.value(), config.resultSetConcurType.value());
        } else {
            statement = conn.createStatement();
        }
        return statement;
    }

    public static PreparedStatement prepareStatement(Connection conn, String sql,
                                                     String[] keyColumnNames,
                                                     Interceptors interceptors,
                                                     boolean returnKeys) throws SQLException {
        PreparedStatement ps;
        StatementInitConfig config = null;
        if (interceptors != null) {
            config = interceptors.getStatementInitConfig();
        }
        if (returnKeys) {
            if (keyColumnNames != null && keyColumnNames.length > 0) {
                ps = conn.prepareStatement(sql, keyColumnNames);
            } else {
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
        } else {
            if (config != null && config.resultSetType != null && config.resultSetConcurType != null) {
                ps = conn.prepareStatement(sql, config.resultSetType.value(), config.resultSetConcurType.value());
            } else {
                ps = conn.prepareStatement(sql);
            }
        }
        return ps;
    }

    public static CallableStatement prepareCall(Connection conn, String sql, Interceptors interceptors)
        throws SQLException {
        CallableStatement cs;
        StatementInitConfig config = null;
        if (interceptors != null) {
            config = interceptors.getStatementInitConfig();
        }
        if (config != null && config.resultSetType != null && config.resultSetConcurType != null) {
            cs = conn.prepareCall(sql, config.resultSetType.value(), config.resultSetConcurType.value());
        } else {
            cs = conn.prepareCall(sql);
        }
        return cs;
    }

    public static boolean before(Interceptors interceptors, Statement statement) throws SQLException {
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors.getInterceptors()) {
                if (!interceptor.beforeExecution(statement)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void after(Interceptors interceptors, Statement statement) throws SQLException {
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors.getInterceptors()) {
                interceptor.afterExecution(statement);
            }
        }
    }

}
