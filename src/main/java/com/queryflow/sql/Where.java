package com.queryflow.sql;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.Assert;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Where<T> {

    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String AND_NEW = ") AND (";
    private static final String OR_NEW = ") OR (";

    protected List<Object> values = new LinkedList<>();
    protected StringBuilder appender = new StringBuilder();
    protected boolean hasWhere = false;

    public T where() {
        appender.append(" WHERE (");
        hasWhere = true;
        return (T) this;
    }

    // a.b = c.b
    public T where(String sentence) {
        appender.append(" WHERE (").append(sentence);
        hasWhere = true;
        return (T) this;
    }

    public T exists(Select select) {
        Assert.notNull(select);
        Assert.notThis(select, this);
        appender.append(" EXISTS (").append(select.buildSql()).append(") ");
        return (T) this;
    }

    public T and() {
        appender.append(AND);
        return (T) this;
    }

    // a.b = c.b
    public T and(String sentence) {
        appender.append(AND).append(sentence);
        return (T) this;
    }

    public T andNew() {
        appender.append(AND_NEW);
        return (T) this;
    }

    public T or() {
        appender.append(OR);
        return (T) this;
    }

    public T or(String sentence) {
        appender.append(OR).append(sentence);
        return (T) this;
    }

    public T orNew() {
        appender.append(OR_NEW);
        return (T) this;
    }

    public T isNull(String column) {
        appender.append(column).append(" IS NULL ");
        return (T) this;
    }

    public T isNull(boolean condition, String column) {
        if (condition) {
            isNull(column);
        }
        return (T) this;
    }

    public T isNotNull(String column) {
        appender.append(column).append(" IS NOT NULL ");
        return (T) this;
    }

    public T isNotNull(boolean condition, String column) {
        if (condition) {
            isNotNull(column);
        }
        return (T) this;
    }

    public T like(String column, Object value) {
        appender.append(column).append(" LIKE ?");
        values.add(value);
        return (T) this;
    }

    public T like(boolean condition, String column, Object value) {
        if (condition) {
            like(column, value);
        }
        return (T) this;
    }

    public T notLike(String column, Object value) {
        appender.append(column).append(" NOT LIKE ?");
        values.add(value);
        return (T) this;
    }

    public T notLike(boolean condition, String column, Object value) {
        if (condition) {
            notLike(column, value);
        }
        return (T) this;
    }

    public T in(String column, Object... values) {
        appender.append(column).append(" IN (");
        if (values == null || values.length == 0) {
            throw new QueryFlowException("");
        }
        addValueAndMark(values);
        appender.append(")");
        return (T) this;
    }

    public T in(boolean condition, String column, Object... values) {
        if (condition) {
            in(column, values);
        }
        return (T) this;
    }

    public T notIn(String column, Object... values) {
        appender.append(column).append(" NOT IN (");
        if (values == null || values.length == 0) {
            throw new QueryFlowException("");
        }
        addValueAndMark(values);
        appender.append(") ");
        return (T) this;
    }

    public T notIn(boolean condition, String column, Object... values) {
        if (condition) {
            notIn(column, values);
        }
        return (T) this;
    }

    private void addValueAndMark(Object... values) {
        Object value;
        for (int i = 0, len = values.length; i < len; i++) {
            value = values[i];
            appender.append("?");
            if (i < len - 1) {
                appender.append(", ");
            }
            this.values.add(value);
        }
    }

    public T gt(String column, Object value) {
        appender.append(column).append(" > ?");
        values.add(value);
        return (T) this;
    }

    public T gt(boolean condition, String column, Object value) {
        if (condition) {
            gt(column, value);
        }
        return (T) this;
    }

    public T ge(String column, Object value) {
        appender.append(column).append(" >= ");
        values.add(value);
        return (T) this;
    }

    public T ge(boolean condition, String column, Object value) {
        if (condition) {
            ge(column, value);
        }
        return (T) this;
    }

    public T lt(String column, Object value) {
        appender.append(column).append(" < ?");
        values.add(value);
        return (T) this;
    }

    public T lt(boolean condition, String column, Object value) {
        if (condition) {
            lt(column, value);
        }
        return (T) this;
    }

    public T le(String column, Object value) {
        appender.append(column).append(" <= ?");
        values.add(value);
        return (T) this;
    }

    public T le(boolean condition, String column, Object value) {
        if (condition) {
            le(column, value);
        }
        return (T) this;
    }

    public T between(String column, Object smallValue, Object bigValue) {
        appender.append(column).append(" BETWEEN ? AND ?");
        values.add(smallValue);
        values.add(bigValue);
        return (T) this;
    }

    public T between(boolean condition, String column, Object smallValue, Object bigValue) {
        if (condition) {
            between(column, smallValue, bigValue);
        }
        return (T) this;
    }

    public T notBetween(String column, Object smallValue, Object bigValue) {
        appender.append(column).append(" NOT BETWEEN ? AND ?");
        values.add(smallValue);
        values.add(bigValue);
        return (T) this;
    }

    public T notBetween(boolean condition, String column, Object smallValue, Object bigValue) {
        if (condition) {
            notBetween(column, smallValue, bigValue);
        }
        return (T) this;
    }

    public T eq(String column, Object value) {
        appender.append(column).append(" = ?");
        this.values.add(value);
        return (T) this;
    }

    public T eq(boolean condition, String column, Object value) {
        if (condition) {
            eq(column, value);
        }
        return (T) this;
    }

    public T notEq(String column, Object value) {
        appender.append(column).append(" <> ?");
        this.values.add(value);
        return (T) this;
    }

    public T notEq(boolean condition, String column, Object value) {
        if (condition) {
            notEq(column, value);
        }
        return (T) this;
    }

    public List<Object> getValues() {
        return values;
    }

}
