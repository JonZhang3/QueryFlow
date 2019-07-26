package com.queryflow.accessor.runner;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.interceptor.Interceptors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * SQL 执行器
 *
 * @author Jon
 * @since 1.0.0
 */
public interface SqlRunner {

    <T> T query(Connection conn, String sql, List<Object> params, Interceptors interceptors,
                ResultSetHandler<T> handler) throws SQLException;

    int update(Connection conn, String sql, List<Object> params, Interceptors interceptors) throws SQLException;

    <T> T insertGetKey(Connection conn, String sql, List<Object> params,
                       String[] keyColumnNames,
                       Interceptors interceptors,
                       ResultSetHandler<T> handler) throws SQLException;

    int[] batch(Connection conn, String sql, List<List<Object>> params, Interceptors interceptors) throws SQLException;

    int[] batch(Connection conn, List<String> sqls, Interceptors interceptors) throws SQLException;

    <T> T batchInsertGetKeys(Connection conn, String sql, List<List<Object>> params,
                             String[] keyColumnNames,
                             Interceptors interceptors,
                             ResultSetHandler<T> handler) throws SQLException;

    int call(Connection conn, String sql, List<Object> params, Interceptors interceptors) throws SQLException;

    <T> T call(Connection conn, String sql, List<Object> params, Interceptors interceptors,
               ResultSetHandler<T> handler) throws SQLException;

}
