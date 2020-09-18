package com.queryflow.accessor;

import com.queryflow.cache.DictionaryTableCache;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.utils.JdbcUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class DictionaryTableManager {

    private static final Map<String, DictionaryTableCache> CACHES = new LinkedHashMap<>();

    protected static void init(String tag, DataSource dataSource, DatabaseConfig config) throws SQLException {
        Connection conn = null;
        ResultSet tables = null;
        DictionaryTableCache cache = new DictionaryTableCache();
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String prefix = config.getDicTablePrefix();
            tables = metaData.getTables(null, null, prefix + ".+", null);
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                getTableDetail(conn, tableName);
            }
            Set<String> dicTables = config.getDicTables();
            if(dicTables != null && dicTables.size() > 0) {
                for (String dicTable : dicTables) {
                    getTableDetail(conn, dicTable);
                }
            }
            CACHES.put(tag, cache);
        } finally {
            JdbcUtil.close(tables);
            JdbcUtil.close(conn);
        }
    }

    public static void get(String tag, String tableName) {

    }



    private static Map<Object, String> getTableDetail(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE `status` = ? ORDER BY `index`";
        Map<Object, String> result = new LinkedHashMap<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "1");
            rs = ps.executeQuery();
            while (rs.next()) {

            }
            return result;
        } finally {
            JdbcUtil.close(rs, ps);
        }
    }

}
