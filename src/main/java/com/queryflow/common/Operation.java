package com.queryflow.common;

public enum Operation {

    SELECT,
    UPDATE,
    INSERT,
    DELETE;

    public static boolean isInsertOrUpdate(Operation operation) {
        return operation == INSERT || operation == UPDATE;
    }

}
