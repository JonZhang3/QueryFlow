package com.queryflow.accessor;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.statement.*;
import com.queryflow.common.TransactionLevel;
import com.queryflow.page.Pager;
import com.queryflow.common.ResultMap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public final class A {

    private A() {
    }

    public static Accessor tag(String tag) {
        return AccessorManager.accessor(tag);
    }

    public static Accessor index(int index) {
        return AccessorManager.accessor(index);
    }

    public static DataSource getDataSource() {
        return AccessorManager.accessor().getDataSource();
    }

    public static Connection getCurrentConnection() {
        return AccessorManager.accessor().getCurrentConnection();
    }

    public static void setCurrentConnection(Connection connection) {
        AccessorManager.accessor().setCurrentConnection(connection);
    }

    public static UpdateStatement createUpdate(String sql) {
        return AccessorManager.accessor().createUpdate(sql);
    }

    public static int update(String sql, Object... values) {
        return AccessorManager.accessor().update(sql, values);
    }

    public static int update(String sql, List<Object> values) {
        return AccessorManager.accessor().update(sql, values);
    }

    public static BatchStatement createBatch() {
        return AccessorManager.accessor().createBatch();
    }

    public static int[] batch(String... sqls) {
        return AccessorManager.accessor().batch(sqls);
    }

    public static int[] batch(List<String> sqls) {
        return AccessorManager.accessor().batch(sqls);
    }

    public static PreparedBatchStatement prepareBatch(String sql) {
        return AccessorManager.accessor().prepareBatch(sql);
    }

    public static int[] batch(String sql, List<List<Object>> values) {
        return AccessorManager.accessor().batch(sql, values);
    }

    public static SelectStatement createQuery(String sql) {
        return AccessorManager.accessor().createQuery(sql);
    }

    public static SelectStatement query(String sql, Object... values) {
        return AccessorManager.accessor().query(sql, values);
    }

    public static SelectStatement query(String sql, List<Object> values) {
        return AccessorManager.accessor().query(sql, values);
    }

    public static Pager<ResultMap> pageToMap(String sql, List<Object> values, int page, int limit) {
        return AccessorManager.accessor().pageToMap(sql, values, page, limit);
    }

    public static Pager<ResultMap> pageToMap(String sql, List<Object> values, int page) {
        return AccessorManager.accessor().pageToMap(sql, values, page);
    }

    public static <T> Pager<T> page(String sql, int page, int limit, Class<T> requiredType, Object... values) {
        return AccessorManager.accessor().page(sql, page, limit, requiredType, values);
    }

    public static <T> Pager<T> page(String sql, List<Object> values, int page, int limit, Class<T> requiredType) {
        return AccessorManager.accessor().page(sql, values, page, limit, requiredType);
    }

    public static <T> Pager<T> page(String sql, List<Object> values, int page, Class<T> requiredType) {
        return AccessorManager.accessor().page(sql, values, page, requiredType);
    }

    public static <T> Pager<T> page(String sql, List<Object> values, int page, ResultSetHandler<List<T>> handler) {
        return AccessorManager.accessor().page(sql, values, page, handler);
    }

    public static <T> Pager<T> page(String sql, List<Object> values, int page, int limit, ResultSetHandler<List<T>> handler) {
        return AccessorManager.accessor().page(sql, values, page, limit, handler);
    }

    public static int count(String sql, Object... values) {
        return AccessorManager.accessor().count(sql, values);
    }

    public static int count(String sql, List<Object> values) {
        return AccessorManager.accessor().count(sql, values);
    }

    public static CallStatement createCall(String sql) {
        return AccessorManager.accessor().createCall(sql);
    }

    public static CallStatement call(String sql, Object... values) {
        return AccessorManager.accessor().call(sql, values);
    }

    public static CallStatement call(String sql, List<Object> values) {
        return AccessorManager.accessor().call(sql, values);
    }

    public static void openTransaction() {
        AccessorManager.accessor().openTransaction();
    }

    public static void openTransaction(TransactionLevel level) {
        AccessorManager.accessor().openTransaction(level);
    }

    public static void commit() {
        AccessorManager.accessor().commit();
    }

    public static void commit(boolean close) {
        AccessorManager.accessor().commit(close);
    }

    public static void rollback() {
        AccessorManager.accessor().rollback();
    }

    public static void rollback(boolean close) {
        AccessorManager.accessor().rollback(close);
    }

    public static <T> T getMapper(Class<T> clazz) {
        return AccessorManager.accessor().getMapper(clazz);
    }

    public static void close() {
        AccessorManager.accessor().close();
    }

}
