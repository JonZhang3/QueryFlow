package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Utils;

import java.util.List;

public class Select extends Where<Select> {

    private StringBuilder orderBy = new StringBuilder();
    private StringBuilder groupBy = new StringBuilder();
    private String havingCondition;

    public Select(String... columns) {
        if (columns == null || columns.length == 0) {
            appender.append("SELECT *");
        } else {
            appender.append("SELECT ");
            String column;
            for (int i = 0, len = columns.length; i < len; i++) {
                column = columns[i];
                appender.append(column);
                if (i < len - 1) {
                    appender.append(", ");
                }
            }
        }
    }

    public Select from(String tables) {
        appender.append(" FROM ").append(tables);
        return this;
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
        if (orderType == null) {
            orderType = OrderType.DESC;
        }
        orderBy.append(column).append(' ').append(orderType.name()).append(",");
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
        StringBuilder sql = new StringBuilder(appender);
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
