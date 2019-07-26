package com.queryflow.page;

import com.queryflow.common.DbType;

public class MysqlPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.MYSQL.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        StringBuilder pageSql = new StringBuilder(sql);
        pageSql.append(" LIMIT ").append(limit).append(" OFFSET ").append(start);
        return pageSql.toString();
    }

}
