package com.queryflow.page.impl;

import com.queryflow.common.DbType;

public class MysqlPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.MYSQL.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        return sql + " LIMIT " + limit + " OFFSET " + start;
    }

}
