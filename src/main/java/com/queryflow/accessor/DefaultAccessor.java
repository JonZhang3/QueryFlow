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
import com.queryflow.page.PageSqlMatchProcess;
import com.queryflow.page.PageSqlProcessSelector;
import com.queryflow.page.Pager;
import com.queryflow.page.SimplePageSqlProcessSelector;
import com.queryflow.mapper.MapperManager;
import com.queryflow.utils.*;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public UpdateStatement createUpdate(String sql) {
        return new UpdateStatement(sql, executor);
    }

    @Override
    public int update(String sql, Object... values) {
        int rows = createUpdate(sql).bindArray(values).execute();
        if (GlobalConfig.isCloseAfterExecuted()) {
            close();
        }
        return rows;
    }

    @Override
    public int update(String sql, List<Object> values) {
        int rows = createUpdate(sql).bindList(values).execute();
        if (GlobalConfig.isCloseAfterExecuted()) {
            close();
        }
        return rows;
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
            int[] rows = batch.execute();
            if (GlobalConfig.isCloseAfterExecuted()) {
                close();
            }
            return rows;
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
            int[] rows = preparedBatch.execute();
            if (GlobalConfig.isCloseAfterExecuted()) {
                close();
            }
            return rows;
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
        return page(sql, values, page, limit, ResultMap.class);
    }

    @Override
    public Pager<ResultMap> pageToMap(String sql, List<Object> values, int page) {
        return page(sql, values, page, 0, ResultMap.class);
    }

    @Override
    public <T> Pager<T> page(String sql, int page, int limit, Class<T> requiredType, Object... values) {
        return page(sql, Arrays.asList(values), page, limit, requiredType);
    }

    @Override
    public <T> Pager<T> page(String sql, List<Object> values, int page, int limit, Class<T> requiredType) {
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
        List records;
        SelectStatement query = createQuery(pageSql).bindList(values);
        if (Map.class.isAssignableFrom(requiredType)) {
            records = query.listMap();
        } else {
            records = query.list(requiredType);
        }
        pager.setRecords(records);
        if (GlobalConfig.isCloseAfterExecuted()) {
            close();
        }
        return pager;
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
        List<T> result = statement.result(handler);
        pager.setRecords(result);
        if (GlobalConfig.isCloseAfterExecuted()) {
            close();
        }
        return pager;
    }

    @Override
    public int count(String sql, Object... values) {
        return count(sql, Arrays.asList(values));
    }

    @Override
    public int count(String sql, List<Object> values) {
        return count(sql, values, GlobalConfig.isCloseAfterExecuted());
    }

    private int count(String sql, List<Object> params, boolean close) {
        Number number = createQuery(getCountSql(sql, params)).bindList(params).one(Number.class);
        if (close) {
            close();
        }
        return number != null ? number.intValue() : 0;
    }

    private String getCountSql(String sql, List<Object> values) {
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
    public void commit() {
        commit(true);
    }

    @Override
    public void commit(boolean close) {
        executor.commit();
        if (close) {
            executor.close();
            log.info("commit and close the connection");
        } else {
            log.info("commit the connection");
        }
    }

    @Override
    public void rollback() {
        rollback(true);
    }

    @Override
    public void rollback(boolean close) {
        executor.rollback();
        if (close) {
            executor.close();
            log.info("rollback and close the connection");
        } else {
            log.info("rollback the connection");
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

}
