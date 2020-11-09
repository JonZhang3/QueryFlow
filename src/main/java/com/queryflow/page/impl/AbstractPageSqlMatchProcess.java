package com.queryflow.page.impl;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.queryflow.page.PageSqlMatchProcess;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPageSqlMatchProcess implements PageSqlMatchProcess {

    protected List<Object> countValues;

    @Override
    public boolean isMatch(String dbType) {
        return dbType.toLowerCase().contains(dbType().toLowerCase());
    }

    @Override
    public String sqlProcess(String sql, int start, int limit) {
        try {
            return PagerUtils.limit(sql, dbType().toLowerCase(), start, limit);
        } catch (Exception e) {
            return internalSqlProcess(sql, start, limit);
        }
    }

    protected abstract String dbType();

    protected abstract String internalSqlProcess(String sql, int start, int limit);

    @Override
    public String getCountSql(String originSql, List<Object> values) {
        if(values != null) {
            this.countValues = new LinkedList<>();
            this.countValues.addAll(values);
        }
        try {
            if(originSql.indexOf('?') == -1) {
                return PagerUtils.count(originSql, dbType().toLowerCase());
            }

            return PagerUtils.count(originSql, dbType().toLowerCase());
        } catch (Exception e) {
            return "SELECT COUNT(1) FROM (" + originSql + ") count_temp";
        }
    }

    @Override
    public List<Object> getCountValues() {
        return countValues;
    }
}
