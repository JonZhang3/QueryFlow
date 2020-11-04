package com.queryflow.common;

import java.sql.ResultSet;

public enum FetchDirection {

    FETCH_FORWARD(ResultSet.FETCH_FORWARD),
    FETCH_REVERSE(ResultSet.FETCH_REVERSE),
    FETCH_UNKNOWN(ResultSet.FETCH_UNKNOWN);

    private final int value;

    FetchDirection(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static FetchDirection valueOf(int direction) {
        switch (direction) {
            case ResultSet.FETCH_FORWARD:
                return FETCH_FORWARD;
            case ResultSet.FETCH_REVERSE:
                return FETCH_REVERSE;
            case ResultSet.FETCH_UNKNOWN:
                return FETCH_UNKNOWN;
            default:
                return null;
        }
    }

}
