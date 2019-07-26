package com.queryflow.accessor.interceptor;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 拦截器
 *
 * @author Jon
 * @since 1.0.0
 */
public interface Interceptor {

    /**
     * 在 SQL 执行前调用该方法
     */
    default boolean beforeExecution(final Statement statement) throws SQLException {
        return true;
    }

    /**
     * 在 SQL 执行后调用该方法
     */
    default void afterExecution(final Statement statement) throws SQLException {
    }

}
