package com.queryflow.page.impl;

import com.queryflow.page.PageSqlMatchProcess;
import com.queryflow.utils.PagerUtil;

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
            return PagerUtil.limit(sql, dbType().toLowerCase(), start, limit);
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
                return PagerUtil.count(originSql, dbType().toLowerCase());
            }
            return PagerUtil.count(originSql, dbType().toLowerCase(), this.countValues);
        } catch (Exception e) {
            return "SELECT COUNT(1) FROM (" + originSql + ") count_temp";
        }
    }

    @Override
    public List<Object> getCountValues() {
        return countValues;
    }
}
