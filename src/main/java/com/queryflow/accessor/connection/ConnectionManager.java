package com.queryflow.accessor.connection;

import com.queryflow.common.TransactionLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * JDBC 数据库连接管理接口
 * 负责获取数据库连接{@link Connection}, 开启事务，提交事务，回滚事务，关闭数据库连接
 *
 * @author Jon
 * @since 1.0.0
 */
public interface ConnectionManager {

    /**
     * 获取数据源
     *
     * @return {@link DataSource}
     */
    DataSource getDataSource();

    /**
     * 获取数据库连接
     *
     * @return {@link Connection}
     */
    Connection getConnection();

    /**
     * 关闭数据库连接
     */
    void close();

    /**
     * 开启事务
     */
    void openTransaction();

    /**
     * 开启事务，同时指定事务级别
     *
     * @param level 事务级别
     * @see TransactionLevel
     */
    void openTransaction(TransactionLevel level);

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

}
