package com.queryflow.page;

import java.util.List;

public interface PageSqlMatchProcess {

    boolean isMatch(String dbType);

    String sqlProcess(String sql, int start, int limit);

    String getCountSql(String originSql, List<Object> values);

    List<Object> getCountValues();

}
