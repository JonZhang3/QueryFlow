package com.queryflow.common;

public interface ColumnFillStrategy {

    Object fill(Class<?> columnClass, Object value);

}
