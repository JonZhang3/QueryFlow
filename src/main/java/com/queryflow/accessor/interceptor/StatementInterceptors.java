package com.queryflow.accessor.interceptor;

import com.queryflow.common.FetchDirection;

import java.sql.SQLException;
import java.sql.Statement;

public final class StatementInterceptors {

    private StatementInterceptors() {}

    public static Interceptor maxFieldSize(int max) {
        return new Interceptor() {
            @Override
            public boolean beforeExecution(Statement statement) throws SQLException {
                statement.setMaxFieldSize(max);
                return true;
            }
        };
    }

    public static Interceptor queryTimeout(int seconds) {
        return new Interceptor() {
            @Override
            public boolean beforeExecution(Statement statement) throws SQLException {
                statement.setQueryTimeout(seconds);
                return true;
            }
        };
    }

    public static Interceptor fetchSize(int rows) {
        return new Interceptor() {
            @Override
            public boolean beforeExecution(Statement statement) throws SQLException {
                statement.setFetchSize(rows);
                return true;
            }
        };
    }

    public static Interceptor fetchDirection(FetchDirection direction) {
        return new Interceptor() {
            @Override
            public boolean beforeExecution(Statement statement) throws SQLException {
                statement.setFetchDirection(direction.value());
                return true;
            }
        };
    }

    public static Interceptor maxRows(int maxRows) {
        return new Interceptor() {
            @Override
            public boolean beforeExecution(Statement statement) throws SQLException {
                statement.setMaxRows(maxRows);
                return true;
            }
        };
    }

}
