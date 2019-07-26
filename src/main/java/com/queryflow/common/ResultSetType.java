package com.queryflow.common;

import java.sql.ResultSet;

public enum ResultSetType {

    FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    private int value;

    ResultSetType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ResultSetType valueOf(int type) {
        switch (type) {
            case ResultSet.TYPE_FORWARD_ONLY:
                return FORWARD_ONLY;
            case ResultSet.TYPE_SCROLL_INSENSITIVE:
                return SCROLL_INSENSITIVE;
            case ResultSet.TYPE_SCROLL_SENSITIVE:
                return SCROLL_SENSITIVE;
            default:
                return null;
        }
    }

}
