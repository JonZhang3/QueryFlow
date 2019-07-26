package com.queryflow.page;

public abstract class AbstractPageSqlMatchProcess implements PageSqlMatchProcess {

    @Override
    public boolean isMatch(String dbType) {
        return dbType.toLowerCase().contains(dbType().toLowerCase());
    }

    @Override
    public String sqlProcess(String sql, int start, int limit) {
        return internalSqlProcess(sql, start, limit);
    }

    protected abstract String dbType();

    protected abstract String internalSqlProcess(String sql, int start, int limit);

    @Override
    public String getCountSql(String originSql) {
        return "SELECT COUNT(1) FROM (" + originSql + ") count_temp";
    }
}
