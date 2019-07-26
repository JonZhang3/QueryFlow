package com.queryflow.entity;

import com.queryflow.common.DictionaryEnum;

import java.io.Serializable;

public enum SettlementStatus implements DictionaryEnum {
    YES(1, "yes"),
    NO(2, "no");

    private int code;
    private String value;

    SettlementStatus(int code, String value) {
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
}
