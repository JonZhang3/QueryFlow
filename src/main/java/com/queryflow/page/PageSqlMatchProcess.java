package com.queryflow.page;

public interface PageSqlMatchProcess {

    boolean isMatch(String dbType);

    String sqlProcess(String sql, int start, int limit);

    String getCountSql(String originSql);

}
