package com.queryflow.page;

import java.util.List;

public interface PageSqlMatchProcess {

    boolean isMatch(String dbType);

    String sqlProcess(String sql, int start, int limit);

    CountSql getCountSql(String originSql, List<Object> values);

}
