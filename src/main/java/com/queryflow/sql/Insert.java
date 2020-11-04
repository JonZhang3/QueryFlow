package com.queryflow.sql;

import com.queryflow.accessor.AccessorFactory;
import com.queryflow.common.QueryFlowException;
import com.queryflow.accessor.Accessor;
import com.queryflow.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public final class Insert {

    private final String table;
    private final StringBuilder columns;
    private final List<Object> values;
    private final StringBuilder marks;

    public Insert(String table) {
        this.table = table;
        columns = new StringBuilder();
        values = new LinkedList<>();
        marks = new StringBuilder();
    }

    public Insert column(String column, Object value) {
        this.columns.append(column).append(',');
        this.values.add(value);
        this.marks.append("?,");
        return this;
    }

    public Insert columns(String... columns) {
        if (columns != null && columns.length > 0) {
            for (String column : columns) {
                this.columns.append(column).append(',');
            }
        }
        return this;
    }

    public Insert values(Object... values) {
        if (values != null && values.length > 0) {
            for (Object value : values) {
                this.values.add(value);
                this.marks.append("?,");
            }
        }
        return this;
    }

    public String buildSql() {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(table);
        if (columns.length() > 0) {
            sql.append(" (")
                .append(columns.substring(0, columns.length() - 1))
                .append(")");
        }
        if (values.isEmpty()) {
            throw new QueryFlowException("Undefined value to insert");
        }
        sql.append(" VALUES (")
            .append(marks.substring(0, marks.length() - 1))
            .append(")");
        return sql.toString();
    }

    public List<Object> getValues() {
        return this.values;
    }

    public int execute() {
        Accessor accessor = AccessorFactory.accessor();
        return accessor.update(buildSql(), Utils.toArray(values));
    }

    public int execute(String dataSourceTag) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.update(buildSql(), Utils.toArray(values));
    }

}
