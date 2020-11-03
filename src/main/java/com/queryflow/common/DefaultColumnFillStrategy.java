package com.queryflow.common;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DefaultColumnFillStrategy implements ColumnFillStrategy {

    @Override
    public Object fill(Class<?> columnClass, Object value) {
        if(value != null) {
            return value;
        }
        if(columnClass == Date.class) {
            return new Date();
        }
        if(columnClass == java.sql.Date.class) {
            return new java.sql.Date(System.currentTimeMillis());
        }
        if(columnClass == Time.class) {
            return new Time(System.currentTimeMillis());
        }
        if(columnClass == Timestamp.class) {
            return new Timestamp(System.currentTimeMillis());
        }

        return value;
    }

}
