package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Update extends Where<Update> {

    private final String table;
    private final List<String> noValueColumns = new LinkedList<>();
    private final Map<String, Object> columnAndValues = new HashMap<>();

    public Update(String table) {
        Assert.notEmpty(table);

        this.table = table;
    }

    public Update set(String column, Object value) {
        Assert.notEmpty(column);

        columnAndValues.put(column, value);
        return this;
    }

    /**
     * 可指定条件，是否要更新指定的列
     *
     * @param condition 条件，如果为 {@code true}，则添加要更新的列，否则不添加
     * @param column 列名
     * @param value 列值
     * @return {@code Update this}
     * @since 1.2.0
     */
    public Update set(boolean condition, String column, Object value) {
        if (condition) {
            set(column, value);
        }
        return this;
    }

    /**
     * 移除要更新的列
     *
     * @param column 列名
     * @return {@code Update this}
     * @since 1.2.0
     */
    public Update remove(String column) {
        columnAndValues.remove(column);
        return this;
    }

    /**
     * id = id + 1
     *
     * @param sentence 条件
     * @return Update
     */
    public Update set(String sentence) {
        this.noValueColumns.add(sentence);
        return this;
    }

    @Override
    public List<Object> getValues() {
        List<Object> result = new LinkedList<>(columnAndValues.values());
        result.addAll(values);
        return result;
    }

    @Override
    public String buildSql() {
        StringBuilder sql = new StringBuilder("UPDATE ")
            .append(table)
            .append(" SET ");
        sql.append(Utils.join(", ", columnAndValues.keySet(), " = ?"));
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
        return accessor.update(buildSql(), getValues());
    }

    public int execute(String dataSourceTag) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.update(buildSql(), getValues());
    }

}
