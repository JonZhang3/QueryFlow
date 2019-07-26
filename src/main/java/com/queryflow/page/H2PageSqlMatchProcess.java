package com.queryflow.page;

import com.queryflow.common.DbType;

public class H2PageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.H2.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        StringBuilder pageSql = new StringBuilder(sql);
        pageSql.append(" limit ").append(start).append(", ").append(limit);
        return pageSql.toString();
    }

}
