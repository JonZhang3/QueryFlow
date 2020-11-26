package com.queryflow.accessor;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.common.QueryFlowException;
import com.queryflow.accessor.connection.DataExecutor;
import com.queryflow.accessor.statement.*;
import com.queryflow.common.DbType;
import com.queryflow.common.ResultMap;
import com.queryflow.common.TransactionLevel;
import com.queryflow.config.GlobalConfig;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import com.queryflow.page.CountSql;
import com.queryflow.page.PageSqlMatchProcess;
import com.queryflow.page.PageSqlProcessSelector;
import com.queryflow.page.Pager;
import com.queryflow.page.SimplePageSqlProcessSelector;
import com.queryflow.mapper.MapperManager;
import com.queryflow.utils.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class DefaultAccessor implements Accessor {

    private static final Log log = LogFactory.getLog(DefaultAccessor.class);

    private final DataExecutor executor;
    private final PageSqlProcessSelector pageSelector = new SimplePageSqlProcessSelector();
    private final DbType dbType;

    DefaultAccessor(DataSource dataSource) {
        Assert.notNull(dataSource);
        dbType = JdbcUtil.getDbType(dataSource);
        executor = new DataExecutor(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        return executor.getDataSource();
    }

    @Override
    public Connection getCurrentConnection() {
        return executor.getConnection();
    }

    @Override
    public void setAutoClose(boolean autoClose) {
        executor.setAutoClose(autoClose);
    }

    @Override
    public void setCurrentConnection(Connection connection) {
        executor.setConnection(connection);
    }

    @Override
    public UpdateStatement createUpdate(String sql) {
        return new UpdateStatement(sql, executor);
    }

    @Override
    public int update(String sql, Object... values) {
        return createUpdate(sql).bindArray(values).execute();
    }

    @Override
    public int update(String sql, List<Object> values) {
        return createUpdate(sql).bindList(values).execute();
    }

    @Override
    public BatchStatement createBatch() {
        return new BatchStatement(executor);
    }

    @Override
    public int[] batch(String... sqls) {
        return batch(Arrays.asList(sqls));
    }

    @Override
    public int[] batch(List<String> sqls) {
        BatchStatement batch = createBatch();
        if (sqls != null) {
            for (String sql : sqls) {
                batch.add(sql);
            }
            return batch.execute();
        }
        return new int[0];
    }

    @Override
    public PreparedBatchStatement prepareBatch(String sql) {
        return new PreparedBatchStatement(sql, executor);
    }

    @Override
    public int[] batch(String sql, List<List<Object>> values) {
        PreparedBatchStatement preparedBatch = prepareBatch(sql);
        if (values != null) {
            for (List<Object> value : values) {
                preparedBatch.add(value);
            }
            return preparedBatch.execute();
        }
        return new int[0];
    }

    @Override
    public SelectStatement createQuery(String sql) {
        return new SelectStatement(sql, executor);
    }

    @Override
    public SelectStatement query(String sql, Object... values) {
        SelectStatement query = createQuery(sql);
        query.bindArray(values);
        return query;
    }

    @Override
    public SelectStatement query(String sql, List<Object> values) {
        SelectStatement query = new SelectStatement(sql, executor);
        query.bindList(values);
        return query;
    }

    @Override
    public Pager<ResultMap> pageToMap(String sql, List<Object> values, int page, int limit) {
        return page(sql, values, page, limit, SelectStatement::listMap);
    }

    @Override
    public Pager<ResultMap> pageToMap(String sql, List<Object> values, int page) {
        return pageToMap(sql, values, page, 0);
    }

    @Override
    public <T> Pager<T> page(String sql, int page, int limit, Class<T> requiredType, Object... values) {
        return page(sql, Arrays.asList(values), page, limit, requiredType);
    }

    @Override
    public <T> Pager<T> page(String sql, List<Object> values, int page, int limit, Class<T> requiredType) {
        return page(sql, values, page, limit, (Function<SelectStatement, List<T>>) statement -> statement.list(requiredType));
    }

    @Override
    public <T> Pager<T> page(String sql, List<Object> values, int page, Class<T> requiredType) {
        return page(sql, values, page, 0, requiredType);
    }

    @Override
    public <T> Pager<T> page(String sql, List<Object> values, int page, ResultSetHandler<List<T>> handler) {
        return page(sql, values, page, 0, handler);
    }

    @Override
    public <T> Pager<T> page(String sql, List<Object> values, int page, int limit, ResultSetHandler<List<T>> handler) {
        return page(sql, values, page, limit, (Function<SelectStatement, List<T>>) statement -> statement.result(handler));
    }

    private <T> Pager<T> page(String sql, List<Object> values, int page, int limit,
                              Function<SelectStatement, List<T>> function) {
        PageSqlMatchProcess process = pageSelector.select(dbType.value());
        if (process == null) {
            throw new QueryFlowException("not support the database");
        }
        int total = count(sql, values, false);
        int defaultLimit = limit;
        if (defaultLimit <= 0) {
            defaultLimit = GlobalConfig.getDefaultPageLimit();
        }
        Pager<T> pager = new Pager<>(total, page, defaultLimit, null);
        String pageSql = process.sqlProcess(sql, pager.getStart(), limit);
        SelectStatement statement = createQuery(pageSql).bindList(values);
        List<T> result = function.apply(statement);
        pager.setRecords(result);
        return pager;
    }

    @Override
    public int count(String sql, Object... values) {
        return count(sql, Arrays.asList(values));
    }

    @Override
    public int count(String sql, List<Object> values) {
        CountSql countSql = getCountSql(sql, values);
        Number number = createQuery(countSql.getSql()).bindList(countSql.getCountValues()).one(Number.class);
        return number != null ? number.intValue() : 0;
    }

    private CountSql getCountSql(String sql, List<Object> values) {
        PageSqlMatchProcess process = pageSelector.select(dbType.value());
        return process.getCountSql(sql, values);
    }

    @Override
    public CallStatement createCall(String sql) {
        return new CallStatement(sql, executor);
    }

    @Override
    public CallStatement call(String sql, Object... values) {
        return createCall(sql).bindArray(values);
    }

    @Override
    public CallStatement call(String sql, List<Object> values) {
        return createCall(sql).bindList(values);
    }

    @Override
    public void openTransaction() {
        openTransaction(null);
    }

    @Override
    public void openTransaction(TransactionLevel level) {
        executor.openTransaction(level);
        log.info("opened a transaction in current thread");
    }

    @Override
    public void transaction(Runnable runnable, Throwable... rollbackFor) {
        transaction(runnable, null, rollbackFor);
    }

    @Override
    public void transaction(Runnable runnable, TransactionLevel level, Throwable... rollbackFor) {
        if (runnable == null) {
            return;
        }
        try {
            openTransaction(level);
            runnable.run();
            commit(false);
        } catch (Throwable t) {
            rollbackFor(t, rollbackFor);
            throw new QueryFlowException(t);
        }
    }

    @Override
    public void commit() {
        executor.commit();
    }

    @Override
    public void commit(boolean close) {
        try {
            executor.commit();
        } finally {
            if (close) {
                executor.close();
            }
        }
    }

    @Override
    public void rollback() {
        executor.rollback();
    }

    @Override
    public void rollback(boolean close) {
        try {
            executor.rollback();
        } finally {
            if (close) {
                executor.close();
            }
        }
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return MapperManager.getMapperClass(clazz);
    }

    @Override
    public void close() {
        executor.close();
    }

    private void rollbackFor(Throwable t, Throwable... rollbackFor) {
        if (rollbackFor == null || rollbackFor.length == 0) {
            rollback();
        } else {
            for (Throwable throwable : rollbackFor) {
                if (throwable != null && throwable.getClass().isAssignableFrom(t.getClass())) {
                    rollback();
                    break;
                }
            }
        }
    }

}
