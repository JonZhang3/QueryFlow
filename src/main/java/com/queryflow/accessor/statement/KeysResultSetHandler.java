package com.queryflow.accessor.statement;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.common.ResultMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class KeysResultSetHandler implements ResultSetHandler<List<ResultMap>> {

    private String[] columnNames;

    public KeysResultSetHandler(String... columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public List<ResultMap> handle(ResultSet rs) throws SQLException {
        List<ResultMap> result = new LinkedList<>();
        if (columnNames != null && columnNames.length > 0) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            int columnNamesLen = columnNames.length;
            int len = Math.min(columnCount, columnNamesLen);
            while (rs.next()) {
                ResultMap map = new ResultMap();
                for (int i = 1; i <= len; i++) {
                    map.put(columnNames[i - 1], rs.getObject(i));
                }
                result.add(map);
            }
        }
        return result;
    }
}
