package com.queryflow.accessor.runner;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.interceptor.InterceptorHelper;
import com.queryflow.accessor.interceptor.Interceptors;
import com.queryflow.utils.JdbcUtil;
import com.queryflow.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseSqlRunner extends AbstractSqlRunner {

    @Override
    public <T> T query(Connection conn, String sql, List<Object> params, Interceptors interceptors,
                       ResultSetHandler<T> handler) throws SQLException {
        check(conn, sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        try {
            ps = this.prepareStatement(conn, sql, null, interceptors, false);
            this.fillStatement(ps, params);

            if (!InterceptorHelper.before(interceptors, ps)) {
                return null;
            }
            rs = ps.executeQuery();
            InterceptorHelper.after(interceptors, ps);

            if (handler != null) {
                result = handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, params), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(rs, ps);
        }
        return result;
    }

    @Override
    public int update(Connection conn, String sql, List<Object> params, Interceptors interceptors) throws SQLException {
        check(conn, sql);
        PreparedStatement ps = null;
        int rows;
        try {
            ps = this.prepareStatement(conn, sql, null, interceptors, false);
            this.fillStatement(ps, params);
            if (!InterceptorHelper.before(interceptors, ps)) {
                return 0;
            }
            rows = ps.executeUpdate();
            InterceptorHelper.after(interceptors, ps);
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, params), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(ps);
        }
        return rows;
    }

    @Override
    public <T> T insertGetKey(Connection conn, String sql, List<Object> params,
                              String[] keyColumnNames,
                              Interceptors interceptors,
                              ResultSetHandler<T> handler) throws SQLException {
        check(conn, sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        try {
            ps = this.prepareStatement(conn, sql, keyColumnNames, interceptors, true);
            this.fillStatement(ps, params);

            if (!InterceptorHelper.before(interceptors, ps)) {
                return null;
            }
            ps.execute();
            InterceptorHelper.after(interceptors, ps);

            if (handler != null) {
                rs = ps.getGeneratedKeys();
                result = handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, params), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(rs, ps);
        }
        return result;
    }

    @Override
    public <T> T batchInsertGetKeys(Connection conn, String sql, List<List<Object>> params,
                                    String[] keyColumnNames,
                                    Interceptors interceptors,
                                    ResultSetHandler<T> handler) throws SQLException {
        check(conn, sql);
        List<List<Object>> localParams = params;
        if (localParams == null) {
            localParams = new ArrayList<>(0);
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        try {
            ps = prepareStatement(conn, sql, keyColumnNames, interceptors, true);
            for (int i = 0, len = localParams.size(); i < len; i++) {
                this.fillStatement(ps, localParams.get(i));
                ps.addBatch();
            }

            if (!InterceptorHelper.before(interceptors, ps)) {
                return null;
            }
            ps.executeBatch();
            InterceptorHelper.after(interceptors, ps);

            if (handler != null) {
                rs = ps.getGeneratedKeys();
                result = handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, null), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(rs, ps);
        }
        return result;
    }

    @Override
    public int[] batch(Connection conn, String sql, List<List<Object>> params, Interceptors interceptors)
        throws SQLException {
        check(conn, sql);
        List<List<Object>> localParams = params;
        if (localParams == null) {
            localParams = new ArrayList<>(0);
        }
        PreparedStatement ps = null;
        int[] rows = new int[0];
        try {
            ps = this.prepareStatement(conn, sql, null, interceptors, false);
            for (int i = 0, len = localParams.size(); i < len; i++) {
                this.fillStatement(ps, localParams.get(i));
                ps.addBatch();
            }
            if (!InterceptorHelper.before(interceptors, ps)) {
                return rows;
            }
            rows = ps.executeBatch();
            InterceptorHelper.after(interceptors, ps);
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, null), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(ps);
        }
        return rows;
    }

    @Override
    public int[] batch(Connection conn, List<String> sqls, Interceptors interceptors) throws SQLException {
        if (conn == null) {
            throw new SQLException("the connection is null");
        }
        if (sqls == null || sqls.isEmpty()) {
            throw new SQLException("sql statements is null");
        }
        Statement statement = null;
        int[] rows = new int[0];
        try {
            statement = this.statement(conn, interceptors);
            if (!InterceptorHelper.before(interceptors, statement)) {
                return rows;
            }
            for (String sql : sqls) {
                statement.addBatch(sql);
            }
            rows = statement.executeBatch();
            InterceptorHelper.after(interceptors, statement);
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, "", null), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(statement);
        }
        return rows;
    }

    @Override
    public int call(Connection conn, String sql, List<Object> params, Interceptors interceptors) throws SQLException {
        check(conn, sql);
        CallableStatement cs = null;
        int rows = 0;
        try {
            cs = this.prepareCall(conn, sql, interceptors);

            if (!InterceptorHelper.before(interceptors, cs)) {
                return rows;
            }
            this.fillStatement(cs, params);
            rows = cs.executeUpdate();
            InterceptorHelper.after(interceptors, cs);

            this.setOutParameterValue(cs, params);
        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, params), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(cs);
        }
        return rows;
    }

    @Override
    public <T> T call(Connection conn, String sql, List<Object> params, Interceptors interceptors,
                      ResultSetHandler<T> handler) throws SQLException {
        check(conn, sql);
        CallableStatement cs = null;
        ResultSet rs = null;
        T result = null;
        try {
            cs = this.prepareCall(conn, sql, interceptors);
            this.fillStatement(cs, params);

            if (!InterceptorHelper.before(interceptors, cs)) {
                return null;
            }
            cs.execute();
            InterceptorHelper.after(interceptors, cs);

            rs = cs.getResultSet();
            if (handler != null) {
                result = handler.handle(rs);
            }

        } catch (SQLException e) {
            throw new SQLException(buildErrorMessage(e, sql, params), e.getSQLState(), e.getErrorCode());
        } finally {
            JdbcUtil.close(rs, cs);
        }
        return result;
    }

    private void check(Connection conn, String sql) throws SQLException {
        if (conn == null) {
            throw new SQLException("the connection is null");
        }
        if (Utils.isEmpty(sql)) {
            throw new SQLException("sql sql is null");
        }
    }

    private void setOutParameterValue(CallableStatement cs, List<Object> params) throws SQLException {
        if (params != null) {
            Object param;
            for (int i = 0, len = params.size(); i < len; i++) {
                param = params.get(i);
                if (param instanceof OutParameter) {
                    ((OutParameter) param).setValue(cs, i + 1);
                }
            }
        }
    }

}
