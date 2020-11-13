package com.queryflow.accessor.connection;

import com.queryflow.common.QueryFlowException;
import com.queryflow.common.TransactionLevel;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import com.queryflow.utils.Assert;
import com.queryflow.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对数据库连接的事务管理
 *
 * @author Jon
 * @since 1.0.0
 */
public class TransactionConnectionManager {

    private static final Log log = LogFactory.getLog(TransactionConnectionManager.class);

    private static final int STATUS_INIT = 0;
    private static final int STATUS_OPEN = 1;
    private static final int STATUS_COMMIT = 2;
    private static final int STATUS_ROLLBACK = 3;

    private final AtomicInteger status = new AtomicInteger(STATUS_INIT);
    protected Connection connection;

    TransactionConnectionManager(Connection connection) {
        Assert.notNull(connection);

        this.connection = connection;
    }

    Connection getConnection() {
        return this.connection;
    }

    boolean isInit() {
        return status.get() == STATUS_INIT;
    }

    boolean isOpen() {
        return status.get() == STATUS_OPEN;
    }

    boolean isCommit() {
        return status.get() == STATUS_COMMIT;
    }

    boolean isRollback() {
        return status.get() == STATUS_ROLLBACK;
    }

    boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new QueryFlowException(e);
        }
    }

    void open() {
        open(null);
    }

    // 开启事务，并指定事务级别
    void open(TransactionLevel level) {
        if (isInit() || isCommit() || isRollback()) {
            try {
                if (level != null && level.getValue() != connection.getTransactionIsolation()) {
                    connection.setTransactionIsolation(level.getValue());
                }
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }
                status.compareAndSet(STATUS_OPEN, status.get());
            } catch (SQLException e) {
                throw new QueryFlowException(e);
            }
        }
    }

    void close() {
        if (isClosed()) {
            log.warn("the connection has been closed");
            return;
        }
        if (!isOpen()) {// 处于非事务中
            JdbcUtil.close(connection);
        }
    }

    protected void commit() {
        if (isClosed()) {
            log.warn("the connection has been closed");
            return;
        }

        if (isOpen()) {
            try {
                connection.commit();
                status.compareAndSet(STATUS_COMMIT, STATUS_OPEN);
                log.debug("commit success");
            } catch (SQLException e) {
                throw new QueryFlowException(e);
            }
        }
    }

    protected void rollback() {
        if (isClosed()) {
            log.warn("the connection has been closed");
            return;
        }
        if (isOpen()) {
            try {
                connection.rollback();
                status.compareAndSet(STATUS_ROLLBACK, STATUS_OPEN);
            } catch (SQLException e) {
                throw new QueryFlowException(e);
            }
        }
    }

}
