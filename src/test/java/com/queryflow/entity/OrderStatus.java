package com.queryflow.entity;

import com.queryflow.common.DictionaryEnum;

import java.io.Serializable;

public enum OrderStatus implements DictionaryEnum {

    OPEN(1, "open"),
    CLOSE(0, "close");

    private int code;
    private String value;

    OrderStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Serializable getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
            "code=" + code +
            ", value='" + value + '\'' +
            '}';
    }
}
