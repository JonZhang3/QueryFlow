package com.queryflow.page.impl;

import com.queryflow.common.DbType;

public class OraclePageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.ORACLE.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        start += 1;
        return "SELECT * FROM (SELECT row_.*, rownum RN FROM (" +
            sql +
            ") row_ WHERE rownum < " + (start + limit) +
            ") WHERE RN >= " + (start);
    }

}
