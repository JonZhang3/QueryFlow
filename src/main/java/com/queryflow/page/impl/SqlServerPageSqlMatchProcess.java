package com.queryflow.page.impl;

import com.queryflow.common.DbType;

public class SqlServerPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.SQLSERVER.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        StringBuilder pageSql = new StringBuilder();
        int pageEnd = start + limit;
        String newSql = sql.replaceFirst("(?i)select(\\s+distinct\\s+)?", "$0 top(" + pageEnd + ") ");

        pageSql.append("with query as (select inner_query.*, row_number() over (order by current_timestamp) as rn from (");
        pageSql.append(newSql);
        pageSql.append(") inner_query) select * from query where rn between ")
            .append(start).append(" and ").append(pageEnd);
        return pageSql.toString();
    }

}
