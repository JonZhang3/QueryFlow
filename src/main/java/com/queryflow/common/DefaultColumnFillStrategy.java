package com.queryflow.common;

public class DefaultColumnFillStrategy implements ColumnFillStrategy {

    @Override
    public Object fill(Class<?> columnClass, Object value) {
        return value;
    }

}
