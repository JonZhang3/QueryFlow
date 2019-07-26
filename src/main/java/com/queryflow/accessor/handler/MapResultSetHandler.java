package com.queryflow.accessor.handler;

import com.queryflow.utils.JdbcUtil;
import com.queryflow.common.ResultMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 将返回结果中的一行封装成 Map
 *
 * @author Jon
 * @since 1.0.0
 */
public class MapResultSetHandler implements ResultSetHandler<ResultMap> {

    @Override
    public ResultMap handle(ResultSet rs) throws SQLException {
        ResultMap result = new ResultMap();
        if (rs.next()) {
            fillMap(rs, rs.getMetaData(), result);
        }
        return result;
    }

    static void fillMap(ResultSet rs, ResultSetMetaData rsd, Map<String, Object> result) throws SQLException {
        int colCount = rsd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            String colName = JdbcUtil.getColumnName(rsd, i);
            Object value = JdbcUtil.getResultSetValue(rs, i);
            result.put(colName, value);
        }
    }

}
