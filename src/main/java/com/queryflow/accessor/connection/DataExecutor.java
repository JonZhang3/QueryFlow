package com.queryflow.accessor.connection;

import com.queryflow.common.QueryFlowException;
import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.common.TransactionLevel;
import com.queryflow.accessor.handler.BeanListResultSetHandler;
import com.queryflow.accessor.handler.BeanResultSetHandler;
import com.queryflow.accessor.handler.MapListResultSetHandler;
import com.queryflow.accessor.handler.MapResultSetHandler;
import com.queryflow.accessor.interceptor.Interceptors;
import com.queryflow.accessor.runner.BaseSqlRunner;
import com.queryflow.accessor.runner.SqlRunner;
import com.queryflow.config.GlobalConfig;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import com.queryflow.common.ResultMap;
import com.queryflow.utils.SqlInterpolation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库操作实现类
 *
 * @author Jon
 * @since 1.0.0
 */
public class DataExecutor implements ConnectionManager, ConnectionExecutor {

    private static final Log log = LogFactory.getLog(ConnectionManager.class);

    private final ThreadLocal<TransactionConnectionManager> CONN_CONTAINER = new ThreadLocal<>();
    private final ThreadLocal<Boolean> AUTO_CLOSE_TAG = ThreadLocal.withInitial(GlobalConfig::isCloseAfterExecuted);

    private final DataSource dataSource;
    private final SqlRunner runner;
    private final SqlInterpolation interpolation = new SqlInterpolation();

    public DataExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
        this.runner = new BaseSqlRunner();
    }

    /**
     * 返回使用的数据源
     */
    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * 获取原始的数据库链接
     *
     * @return 数据库链接
     */
    @Override
    public Connection getConnection() {
        return getTransactionManager().getConnection();
    }

    /**
     * 为当前线程设置数据库连接。
     * 如果已经存在一个数据库连接并与设置的新连接不是同一个连接，则先关闭当前连接，然后设置新连接
     *
     * @param connection 新的数据库连接
     * @since 1.2.0
     */
    public void setConnection(Connection connection) {
        TransactionConnectionManager manager = CONN_CONTAINER.get();
        if (manager == null) {
            manager = new TransactionConnectionManager(connection);
            CONN_CONTAINER.set(manager);
        } else {
            Connection oldConn = manager.connection;
            if (oldConn != null && oldConn != connection) {
                try {
                    oldConn.close();
                } catch (SQLException e) {
                    throw new QueryFlowException(e);
                }
            }
            manager.connection = connection;
        }
    }

    private TransactionConnectionManager getTransactionManager() {
        return getTransactionManager(true);
    }

    /**
     * 获取数据库链接，如果 {@code ThreadLocal} 中已经有数据库链接，并且不需要重新创建
     *
     * @param create 是否需要新创建一个数据库链接，一般会在关闭数据库链接时，将其设置为 {@code false}；其他情况下
     *               设置为 {@code true}
     * @return 数据库链接
     */
    private TransactionConnectionManager getTransactionManager(boolean create) {
        TransactionConnectionManager transactionConnectionManager = CONN_CONTAINER.get();
        try {
            if (create && (transactionConnectionManager == null || transactionConnectionManager.isClosed())) {
                transactionConnectionManager = new TransactionConnectionManager(getDataSource().getConnection());
                CONN_CONTAINER.set(transactionConnectionManager);
            }
        } catch (SQLException e) {
            throw new QueryFlowException(e);
        }
        return transactionConnectionManager;
    }

    public void setAutoClose(boolean autoClose) {
        AUTO_CLOSE_TAG.set(autoClose);
    }

    /**
     * 关闭数据库链接
     */
    @Override
    public void close() {
        TransactionConnectionManager transactionConnectionManager = getTransactionManager(false);
        if (transactionConnectionManager != null) {
            try {
                transactionConnectionManager.close();
            } finally {
                CONN_CONTAINER.remove();
                AUTO_CLOSE_TAG.remove();
            }
        }
    }

    /**
     * 打开数据库事务
     */
    @Override
    public void openTransaction() {
        getTransactionManager().open();
    }

    /**
     * 打开数据库事务，同时指定事务级别
     *
     * @param level 事务级别
     * @see TransactionLevel
     */
    @Override
    public void openTransaction(TransactionLevel level) {
        getTransactionManager().open(level);
    }

    /**
     * 提交事务
     */
    @Override
    public void commit() {
        getTransactionManager().commit();
    }

    /**
     * 回滚事务
     */
    @Override
    public void rollback() {
        getTransactionManager().rollback();
    }

    @Override
    public int update(String sql, List<Object> params, Interceptors interceptors) {
        logMessage(sql, params);
        try {
            return runner.update(getConnection(), sql, params, interceptors);
        } catch (SQLException e) {
            log.error("update sql error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public <T> T insertGetKey(String sql, List<Object> params, String[] keyColumnNames,
                              Interceptors interceptors, ResultSetHandler<T> handler) {
        logMessage(sql, params);
        try {
            return runner.insertGetKey(getConnection(), sql, params, keyColumnNames, interceptors, handler);
        } catch (SQLException e) {
            log.error("insert sql error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public <T> T batchInsertGetKes(String sql, List<List<Object>> params,
                                   String[] keyColumnNames,
                                   Interceptors interceptors,
                                   ResultSetHandler<T> handler) {
        if (GlobalConfig.isDebug()) {
            log.info("batchUpdate insert sql is: " + sql);
        }
        try {
            return runner.batchInsertGetKeys(getConnection(), sql, params, keyColumnNames, interceptors, handler);
        } catch (SQLException e) {
            log.error("batch insert error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public int[] batchUpdate(List<String> sqls, Interceptors interceptors) {
        try {
            return runner.batch(getConnection(), sqls, interceptors);
        } catch (SQLException e) {
            log.error("batch update error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public int[] batchUpdate(String sql, List<List<Object>> params, Interceptors interceptors) {
        if (GlobalConfig.isDebug()) {
            log.info("the batchUpdate update sql is: " + sql);
        }
        try {
            return runner.batch(getConnection(), sql, params, interceptors);
        } catch (SQLException e) {
            log.error("batch update error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public <T> T query(String sql, List<Object> params, Interceptors interceptors,
                       final ResultSetHandler<T> handler) {
        logMessage(sql, params);
        try {
            return runner.query(getConnection(), sql, params, interceptors, handler);
        } catch (SQLException e) {
            log.error("query error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T queryForBean(String sql, List<Object> params, Interceptors interceptors, Class<T> beanClass) {
        return (T) query(sql, params, interceptors, BeanResultSetHandler.newBeanHandler(beanClass));
    }

    @Override
    public <T> List<T> queryForListBean(String sql, List<Object> params, Interceptors interceptors, Class<T> beanClass) {
        return query(sql, params, interceptors, new BeanListResultSetHandler<T>(beanClass));
    }

    @Override
    public ResultMap queryForMap(String sql, List<Object> params, Interceptors interceptors) {
        return query(sql, params, interceptors, new MapResultSetHandler());
    }

    @Override
    public List<ResultMap> queryForListMap(String sql, List<Object> params, Interceptors interceptors) {
        return query(sql, params, interceptors, new MapListResultSetHandler());
    }

    @Override
    public int call(String sql, List<Object> params, Interceptors interceptors) {
        logMessage(sql, params);
        try {
            return runner.call(getConnection(), sql, params, interceptors);
        } catch (SQLException e) {
            log.error("execute sql error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    @Override
    public <T> T call(String sql, List<Object> params, Interceptors interceptors,
                      final ResultSetHandler<T> handler) {
        logMessage(sql, params);
        try {
            return runner.call(getConnection(), sql, params, interceptors, handler);
        } catch (SQLException e) {
            log.error("execute sql error: ", e);
            throw new QueryFlowException(e);
        } finally {
            tryClose();
        }
    }

    private void tryClose() {
        if (AUTO_CLOSE_TAG.get()) {
            close();
        }
    }

    private void logMessage(String sql, List<Object> params) {
        if (GlobalConfig.isDebug()) {
            log.info("the execute sql is: " + sql);
            log.info("the execute sql with values is: " + interpolation.convert(sql, params));
        }
    }

}
