package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.util.List;

public final class Select extends Where<Select> {

    private final StringBuilder orderBy = new StringBuilder();
    private final StringBuilder groupBy = new StringBuilder();
    private String havingCondition;

    public Select(String... columns) {
        if (columns == null || columns.length == 0) {
            stack.push("SELECT *");
        } else {
            stack.push("SELECT ");
            String column;
            for (int i = 0, len = columns.length; i < len; i++) {
                column = columns[i];
                stack.push(column);
                if (i < len - 1) {
                    stack.push(", ");
                }
            }
        }
    }

    public Select from(String... tables) {
        if (tables == null || tables.length == 0) {
            throw new IllegalArgumentException("must provide a table name");
        }
        stack.push(" FROM ");
        for (int i = 0, len = tables.length; i < len; i++) {
            stack.push(tables[i]);
            if (i < len - 1) {
                stack.push(", ");
            }
        }
        return this;
    }

    public Select from(Select select, String alias) {
        Assert.notNull(select);
        Assert.notThis(select, this);
        if (Utils.isBlank(alias)) {
            throw new IllegalArgumentException("must provide an alias");
        }
        stack.push(" FROM (").push(select.buildSql()).push(") ").push(alias).push(" ");
        this.values.addAll(select.getValues());
        return this;
    }

    public Join join(String tableName) {
        return new Join(this, Join.JoinType.JOIN, tableName);
    }

    public Join join(Select select, String alias) {
        return newJoin(select, alias, Join.JoinType.JOIN);
    }

    public Join leftJoin(String tableName) {
        return new Join(this, Join.JoinType.LEFT_JOIN, tableName);
    }

    public Join leftJoin(Select select, String alias) {
        return newJoin(select, alias, Join.JoinType.LEFT_JOIN);
    }

    public Join rightJoin(String tableName) {
        return new Join(this, Join.JoinType.RIGHT_JOIN, tableName);
    }

    public Join rightJoin(Select select, String alias) {
        return newJoin(select, alias, Join.JoinType.RIGHT_JOIN);
    }

    public Join fullJoin(String tableName) {
        return new Join(this, Join.JoinType.FULL_JOIN, tableName);
    }

    public Join fullJoin(Select select, String alias) {
        return newJoin(select, alias, Join.JoinType.FULL_JOIN);
    }

    private Join newJoin(Select select, String alias, Join.JoinType joinType) {
        Assert.notNull(select);
        Assert.notThis(select, this);
        if (Utils.isBlank(alias)) {
            throw new IllegalArgumentException("must provide an alias");
        }
        Join join = new Join(this, joinType, "(" + select.buildSql() + ") " + alias);
        this.values.addAll(select.values);
        return join;
    }

    public Select groupBy(String column) {
        groupBy.append(column).append(",");
        return this;
    }

    public Select groupBy(String... columns) {
        if (columns != null && columns.length > 0) {
            for (String column : columns) {
                groupBy.append(column).append(",");
            }
        }
        return this;
    }

    public Select having(String condition) {
        if (Utils.isNotEmpty(condition)) {
            this.havingCondition = condition;
        }
        return this;
    }

    public Select having(String condition, Object value) {
        if (Utils.isNotEmpty(condition)) {
            this.havingCondition = condition;
            this.values.add(value);
        }
        return this;
    }

    public Select orderBy(String column) {
        return orderBy(column, null);
    }

    public Select orderBy(String column, OrderType orderType) {
        String orderByType = "";
        if (orderType != null) {
            orderByType = " " + orderType.name();
        }
        orderBy.append(column).append(orderByType).append(",");
        return this;
    }

    public Select orderByDesc(String column) {
        orderBy.append(column).append(" DESC,");
        return this;
    }

    public Select orderByAcs(String column) {
        orderBy.append(column).append(" ACS,");
        return this;
    }

    public String buildSql() {
        StringBuilder sql = new StringBuilder(stack.toStr());
        if (hasWhere) {
            sql.append(")");
        }
        if (groupBy.length() > 0) {
            sql.append(" GROUP BY ").append(groupBy.substring(0, groupBy.length() - 1));
            if (Utils.isNotEmpty(havingCondition)) {
                sql.append(" HAVING ").append(havingCondition);
            }
        }
        if (orderBy.length() > 0) {
            sql.append(" ORDER BY ").append(orderBy.substring(0, orderBy.length() - 1));
        }
        return sql.toString();
    }

    public <T> T query(Class<T> requiredType) {
        Accessor accessor = AccessorFactory.accessor();
        return accessor.query(buildSql(), values).one(requiredType);
    }

    public <T> T query(String dataSourceTag, Class<T> requiredType) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.query(buildSql(), values).one(requiredType);
    }

    public <T> List<T> queryList(Class<T> requiredType) {
        Accessor accessor = AccessorFactory.accessor();
        return accessor.query(buildSql(), values).list(requiredType);
    }

    public <T> List<T> queryList(String dataSourceTag, Class<T> requiredType) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.query(buildSql(), values).list(requiredType);
    }

}
