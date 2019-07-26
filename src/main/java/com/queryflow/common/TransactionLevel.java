package com.queryflow.common;

import java.sql.Connection;

/**
 * 数据库事务级别枚举常量
 */
public enum TransactionLevel {

    NONE(Connection.TRANSACTION_NONE),

    /**
     * 最低级别，脏读、不可重复读、幻读均不可避免
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    /**
     * 该级别可避免脏读
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    /**
     * 该级别可避免脏读和不可重复读
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    /**
     * 该级别可避免脏读、不可重复读、幻读
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

    UNKNOWN(Integer.MIN_VALUE);

    private final int level;

    TransactionLevel(int level) {
        this.level = level;
    }

    public int getValue() {
        return this.level;
    }

    public static TransactionLevel valueOf(int value) {
        switch (value) {
            case Connection.TRANSACTION_NONE:
                return NONE;
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return READ_UNCOMMITTED;
            case Connection.TRANSACTION_READ_COMMITTED:
                return READ_COMMITTED;
            case Connection.TRANSACTION_REPEATABLE_READ:
                return REPEATABLE_READ;
            case Connection.TRANSACTION_SERIALIZABLE:
                return SERIALIZABLE;
            default:
                return UNKNOWN;
        }
    }

}
