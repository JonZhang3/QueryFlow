package com.queryflow.accessor;

import com.queryflow.cache.DictionaryTableCache;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.utils.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DictionaryTableManager {

    private static final Map<String, DictionaryTableCache> CACHES = new LinkedHashMap<>();

    protected static void init(String tag, DataSource dataSource, DatabaseConfig config) throws SQLException {
        Connection conn = null;
        ResultSet tables = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String prefix = config.getDicTablePrefix();
            tables = metaData.getTables(null, null, prefix + ".+", null);
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

            }
        } finally {
            JdbcUtil.close(tables);
            JdbcUtil.close(conn);
        }
    }

    private static void getTableDetail(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE status = ?";

    }

}
