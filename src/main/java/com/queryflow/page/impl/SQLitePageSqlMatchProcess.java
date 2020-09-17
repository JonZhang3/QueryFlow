package com.queryflow.page.impl;

import com.queryflow.common.DbType;

public class SQLitePageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.SQLITE.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        StringBuilder pageSql = new StringBuilder();
        pageSql.append(" LIMIT ").append(limit).append(" OFFSET ").append(start);
        return pageSql.toString();
    }

}
