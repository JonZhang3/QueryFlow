package com.queryflow.sql;

import com.queryflow.accessor.AccessorFactory;
import com.queryflow.common.QueryFlowException;
import com.queryflow.accessor.Accessor;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Insert implements Statement {

    private final String table;
    private final StringBuilder columns;
    private final List<Object> values;
    private final StringBuilder marks;
    private String selectStatement;

    public Insert(String table) {
        Assert.notEmpty(table);

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

    /**
     * 支持 INSERT INTO [table] SELECT 语句
     *
     * @param select {@code Select}
     * @return {@code this}
     */
    public Insert select(Select select) {
        Assert.notNull(select);
        this.selectStatement = select.buildSql();
        this.values.addAll(select.getValues());
        return this;
    }

    @Override
    public String buildSql() {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(table);
        if (selectStatement != null) {
            if (columns.length() > 0) {
                sql.append(" (")
                    .append(columns.substring(0, columns.length() - 1))
                    .append(")");
            }
            sql.append(" ").append(selectStatement);
        } else {
            if (columns.length() > 0) {
                sql.append(" (")
                    .append(columns.substring(0, columns.length() - 1))
                    .append(")");
            }
            sql.append(" VALUES (")
                .append(marks.substring(0, marks.length() - 1))
                .append(")");
        }
        return sql.toString();
    }

    @Override
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
