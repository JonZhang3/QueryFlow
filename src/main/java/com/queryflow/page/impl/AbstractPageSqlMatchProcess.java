package com.queryflow.page.impl;

import com.queryflow.page.CountSql;
import com.queryflow.page.PageSqlMatchProcess;
import com.queryflow.utils.PagerUtil;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPageSqlMatchProcess implements PageSqlMatchProcess {

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
    public CountSql getCountSql(String originSql, List<Object> values) {
        List<Object> countValues = new LinkedList<>();
        if (values != null) {
            countValues.addAll(values);
        }
        String sql = null;
        try {
            if (originSql.indexOf('?') == -1) {
                sql = PagerUtil.count(originSql, dbType().toLowerCase());
            } else {
                sql = PagerUtil.count(originSql, dbType().toLowerCase(), countValues);
            }
        } catch (Exception ignore) {
        }
        if (sql == null) {
            sql = "SELECT COUNT(1) FROM (" + originSql + ") count_temp";
        }
        return new CountSql(sql, countValues);
    }

}
