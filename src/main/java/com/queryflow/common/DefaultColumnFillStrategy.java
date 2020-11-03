package com.queryflow.common;

import com.queryflow.utils.Utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @since 1.2.0
 * @author Jon
 */
public class DefaultColumnFillStrategy implements ColumnFillStrategy {

    public static final String DEFAULT_FILL_PATTERN = "yyyyMMddHHmmss";

    @Override
    public Object fill(Class<?> columnClass, Object value, String pattern, Operation operation) {
        // 如果手动设定过值，则直接返回
        if (value != null) {
            return value;
        }
        if (columnClass == Date.class) {
            return new Date();
        }
        if (columnClass == java.sql.Date.class) {
            return new java.sql.Date(System.currentTimeMillis());
        }
        if (columnClass == Time.class) {
            return new Time(System.currentTimeMillis());
        }
        if (columnClass == Timestamp.class) {
            return new Timestamp(System.currentTimeMillis());
        }
        if (columnClass == String.class) {
            if (Utils.isNotBlank(pattern)) {
                return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
            }
        }
        return null;
    }

}
