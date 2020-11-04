package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public final class Update extends Where<Update> {

    private final String table;
    private final List<String> columns;
    private final List<String> noValueColumns;

    public Update(String table) {
        this.table = table;
        columns = new LinkedList<>();
        noValueColumns = new LinkedList<>();
    }

    public Update set(String column, Object value) {
        this.columns.add(column);
        this.values.add(value);
        return this;
    }

    /**
     * id = id + 1
     *
     * @param condition 条件
     * @return Update
     */
    public Update set(String condition) {
        this.noValueColumns.add(condition);
        return this;
    }

    public String buildSql() {
        StringBuilder sql = new StringBuilder("UPDATE ")
            .append(table)
            .append(" SET ");
        sql.append(Utils.join(", ", columns, " = ?"));
        if (!noValueColumns.isEmpty()) {
            sql.append(", ");
        }
        sql.append(Utils.join(", ", noValueColumns)).append(stack.toStr());
        if (hasWhere) {
            sql.append(")");
        }
        return sql.toString();
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
