package com.queryflow.common;

import java.sql.ResultSet;

public enum ResultSetConcurType {

    READ_ONLY(ResultSet.CONCUR_READ_ONLY),
    UPDATABLE(ResultSet.CONCUR_UPDATABLE);

    private final int value;

    ResultSetConcurType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ResultSetConcurType valueOf(int type) {
        switch (type) {
            case ResultSet.CONCUR_READ_ONLY:
                return READ_ONLY;
            case ResultSet.CONCUR_UPDATABLE:
                return UPDATABLE;
            default:
                return null;
        }
    }

}
