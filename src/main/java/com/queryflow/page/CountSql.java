package com.queryflow.page;

import java.util.List;

/**
 * CountSql
 *
 * @since 1.2.0
 * @author Jon
 */
public class CountSql {

    private final String sql;
    private final List<Object> values;

    public CountSql(String sql, List<Object> values) {
        this.sql = sql;
        this.values = values;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getCountValues() {
        return values;
    }
}
