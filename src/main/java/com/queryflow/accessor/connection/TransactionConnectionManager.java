package com.queryflow.accessor.connection;

import com.queryflow.common.QueryFlowException;
import com.queryflow.common.TransactionLevel;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import com.queryflow.utils.Assert;
import com.queryflow.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

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

    private int status;
    protected final Connection connection;

    TransactionConnectionManager(Connection connection) {
        Assert.notNull(connection);

        status = STATUS_INIT;
        this.connection = connection;
    }

    Connection getConnection() {
        return this.connection;
    }

    boolean isOpen() {
        return status == STATUS_OPEN;
    }

    boolean isCommit() {
        return status == STATUS_COMMIT;
    }

    boolean isRollback() {
        return status == STATUS_ROLLBACK;
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
        if (status == STATUS_INIT || isCommit() || isRollback()) {
            try {
                connection.setAutoCommit(false);
                if (level != null && level.getValue() != connection.getTransactionIsolation()) {
                    connection.setTransactionIsolation(level.getValue());
                }
                status = STATUS_OPEN;
            } catch (SQLException e) {
                throw new QueryFlowException(e);
            }
        }
    }

    void close() {
        if (isClosed()) {
            log.debug("the connection has been closed");
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
                status = STATUS_COMMIT;
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
                status = STATUS_ROLLBACK;
            } catch (SQLException e) {
                throw new QueryFlowException(e);
            }
        }
    }

}
