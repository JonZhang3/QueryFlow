package com.queryflow.sql;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Stack;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Where<T> {

    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String AND_NEW = ") AND (";
    private static final String OR_NEW = ") OR (";
    private static final String WHERE = " WHERE (";

    protected List<Object> values = new LinkedList<>();
    protected Stack<String> stack = new Stack<>();
    protected boolean hasWhere = false;
    protected String lastFactor;

    public T where() {
        stack.push(WHERE);
        hasWhere = true;
        return (T) this;
    }

    // a.b = c.b
    public T where(String sentence) {
        stack.push(WHERE).push(sentence);
        hasWhere = true;
        return (T) this;
    }

    public T exists(Select select) {
        Assert.notNull(select);
        if (this instanceof Select) {
            Assert.notThis(select, this);
        }
        stack.push(" EXISTS (").push(select.buildSql()).push(") ");
        return (T) this;
    }

    public T exists(boolean condition, Select select) {
        if (condition) {
            checkAndPushFactor();
            exists(select);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T and() {
        checkAndPushWhere(AND, null);
        return (T) this;
    }

    // a.b = c.b
    public T and(String sentence) {
        checkAndPushWhere(AND, sentence);
        return (T) this;
    }

    public T andNew() {
        checkAndPushWhere(AND_NEW, null);
        return (T) this;
    }

    public T or() {
        checkAndPushWhere(OR, null);
        return (T) this;
    }

    public T or(String sentence) {
        checkAndPushWhere(AND, sentence);
        return (T) this;
    }

    public T orNew() {
        checkAndPushWhere(OR_NEW, null);
        return (T) this;
    }

    public T isNull(String column) {
        stack.push(column).push(" IS NULL ");
        return (T) this;
    }

    public T isNull(boolean condition, String column) {
        if (condition) {
            checkAndPushFactor();
            isNull(column);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T isNotNull(String column) {
        stack.push(column).push(" IS NOT NULL ");
        return (T) this;
    }

    public T isNotNull(boolean condition, String column) {
        if (condition) {
            checkAndPushFactor();
            isNotNull(column);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T like(String column, Object value) {
        stack.push(column).push(" LIKE ?");
        values.add(value);
        return (T) this;
    }

    public T like(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            like(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T notLike(String column, Object value) {
        stack.push(column).push(" NOT LIKE ?");
        values.add(value);
        return (T) this;
    }

    public T notLike(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            notLike(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T in(String column, Object... values) {
        stack.push(column).push(" IN (");
        if (values == null || values.length == 0) {
            throw new QueryFlowException("");
        }
        addValueAndMark(values);
        stack.push(")");
        return (T) this;
    }

    public T in(boolean condition, String column, Object... values) {
        if (condition) {
            checkAndPushFactor();
            in(column, values);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T in(Select select) {
        Assert.notNull(select);
        if (this instanceof Select) {
            Assert.notThis(select, this);
        }
        stack.push(" IN (").push(select.buildSql()).push(")");
        values.addAll(select.getValues());
        return (T) this;
    }

    public T in(boolean condition, Select select) {
        if (condition) {
            checkAndPushFactor();
            in(select);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T notIn(String column, Object... values) {
        stack.push(column).push(" NOT IN (");
        if (values == null || values.length == 0) {
            throw new QueryFlowException("");
        }
        addValueAndMark(values);
        stack.push(") ");
        return (T) this;
    }

    public T notIn(boolean condition, String column, Object... values) {
        if (condition) {
            checkAndPushFactor();
            notIn(column, values);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T notIn(Select select) {
        Assert.notNull(select);
        if (this instanceof Select) {
            Assert.notThis(select, this);
        }
        stack.push(" NOT IN (").push(select.buildSql()).push(") ");
        values.addAll(select.getValues());
        return (T) this;
    }

    public T notIn(boolean condition, Select select) {
        if (condition) {
            checkAndPushFactor();
            notIn(select);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T gt(String column, Object value) {
        stack.push(column).push(" > ?");
        values.add(value);
        return (T) this;
    }

    public T gt(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            gt(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T ge(String column, Object value) {
        stack.push(column).push(" >= ");
        values.add(value);
        return (T) this;
    }

    public T ge(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            ge(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T lt(String column, Object value) {
        stack.push(column).push(" < ?");
        values.add(value);
        return (T) this;
    }

    public T lt(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            lt(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T le(String column, Object value) {
        stack.push(column).push(" <= ?");
        values.add(value);
        return (T) this;
    }

    public T le(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            le(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T between(String column, Object smallValue, Object bigValue) {
        stack.push(column).push(" BETWEEN ? AND ?");
        values.add(smallValue);
        values.add(bigValue);
        return (T) this;
    }

    public T between(boolean condition, String column, Object smallValue, Object bigValue) {
        if (condition) {
            checkAndPushFactor();
            between(column, smallValue, bigValue);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T notBetween(String column, Object smallValue, Object bigValue) {
        stack.push(column).push(" NOT BETWEEN ? AND ?");
        values.add(smallValue);
        values.add(bigValue);
        return (T) this;
    }

    public T notBetween(boolean condition, String column, Object smallValue, Object bigValue) {
        if (condition) {
            checkAndPushFactor();
            notBetween(column, smallValue, bigValue);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T eq(String column, Object value) {
        stack.push(column).push(" = ?");
        this.values.add(value);
        return (T) this;
    }

    public T eq(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            eq(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public T notEq(String column, Object value) {
        stack.push(column).push(" <> ?");
        this.values.add(value);
        return (T) this;
    }

    public T notEq(boolean condition, String column, Object value) {
        if (condition) {
            checkAndPushFactor();
            notEq(column, value);
        } else {
            checkAndPopFactor();
        }
        return (T) this;
    }

    public List<Object> getValues() {
        return values;
    }

    private void addValueAndMark(Object... values) {
        Object value;
        for (int i = 0, len = values.length; i < len; i++) {
            value = values[i];
            stack.push("?");
            if (i < len - 1) {
                stack.push(", ");
            }
            this.values.add(value);
        }
    }

    private void checkAndPushWhere(String factor, String sentence) {
        if (WHERE.equals(lastFactor)) {
            stack.push(WHERE);
            lastFactor = null;
            hasWhere = true;
        } else {
            stack.push(factor);
        }
        if(sentence != null) {
            stack.push(sentence);
        }
    }

    private void checkAndPushFactor() {
        if (lastFactor != null) {
            stack.push(lastFactor);
            if (WHERE.equals(lastFactor)) {
                hasWhere = true;
            }
            lastFactor = null;
        }
    }

    private void checkAndPopFactor() {
        String str = stack.peek();
        if (AND.equals(str) || OR.equals(str)) {
            stack.pop();
        } else if (AND_NEW.equals(str) || OR_NEW.equals(str)) {
            lastFactor = stack.pop();
        } else if (WHERE.equals(str)) {
            lastFactor = stack.pop();
            hasWhere = false;
        }
    }

}
