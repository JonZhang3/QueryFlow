package com.queryflow.page;

import com.queryflow.common.DbType;

public class DerbyPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return DbType.DERBY.value();
    }

    @Override
    protected String internalSqlProcess(String sql, int start, int limit) {
        return sql + " OFFSET " + start + " ROW" +
            " FETCH NEXT " + limit + " ROW ONLY";
    }

}
