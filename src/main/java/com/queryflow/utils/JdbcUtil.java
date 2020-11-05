package com.queryflow.utils;

import com.queryflow.common.QueryFlowException;
import com.queryflow.common.DbType;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.Instant;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

/**
 * 关于数据库操作的工具类
 *
 * @author Jon
 * @since 1.0.0
 */
public final class JdbcUtil {

    private static final Log log = LogFactory.getLog("queryflow");

    private static final boolean getObjectWithTypeAvailable =
        Utils.getMethod(ResultSet.class, "getObject", int.class, Class.class) != null;

    private JdbcUtil() {
    }

    /**
     * 关闭数据库连接
     *
     * @param conn 数据库连接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close database connection error", e);
            }
        }
    }

    /**
     * 关闭 Statement
     *
     * @param statement Statement
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("close database sql error", e);
            }
        }
    }

    /**
     * 关闭 ResultSet
     *
     * @param resultSet ResultSet
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("close database ResultSet error", e);
            }
        }
    }

    /**
     * 同时关闭 ResultSet 和 Statement
     *
     * @param rs        ResultSet
     * @param statement Statement
     */
    public static void close(ResultSet rs, Statement statement) {
        close(rs);
        close(statement);
    }

    /**
     * 判断连接的数据库是否支持批量操作
     *
     * @param conn 数据库连接
     * @return {@code true} 支持批量操作
     */
    public static boolean supportBatch(Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            if (metaData != null) {
                if (metaData.supportsBatchUpdates()) {
                    log.info("JDBC driver supports batchUpdate updates");
                    return true;
                } else {
                    log.error("JDBC driver does not support batchUpdate updates");
                }
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return false;
    }

    /**
     * 从 JDBC URL 中解析数据库类型
     *
     * @param jdbcUrl JDBC URL，不能为空
     * @return 数据库类型
     * @see DbType
     */
    public static DbType getDbType(String jdbcUrl) {
        Assert.notEmpty(jdbcUrl);

        if (jdbcUrl.startsWith("jdbc:derby:") || jdbcUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return DbType.DERBY;
        } else if (jdbcUrl.startsWith("jdbc:mysql:") || jdbcUrl.startsWith("jdbc:cobar:")
            || jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return DbType.MYSQL;
        } else if (jdbcUrl.startsWith("jdbc:oracle:") || jdbcUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return DbType.ORACLE;
        } else if (jdbcUrl.startsWith("jdbc:microsoft:") || jdbcUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return DbType.SQLSERVER;
        } else if (jdbcUrl.startsWith("jdbc:sqlserver:") || jdbcUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return DbType.SQLSERVER;
        } else if (jdbcUrl.startsWith("jdbc:postgresql:") || jdbcUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return DbType.POSTGRESQL;
        } else if (jdbcUrl.startsWith("jdbc:hsqldb:") || jdbcUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return DbType.HSQL;
        } else if (jdbcUrl.startsWith("jdbc:db2:")) {
            return DbType.DB2;
        } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
            return DbType.SQLITE;
        } else if (jdbcUrl.startsWith("jdbc:h2:") || jdbcUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return DbType.H2;
        } else if (jdbcUrl.startsWith("jdbc:mariadb:")) {
            return DbType.MARIADB;
        } else {
            return DbType.OTHER;
        }
    }

    /**
     * 获取数据库类型。首先从数据库库连接中获取 JDBC URL，然后解析 URL 获取数据库类型
     *
     * @param dataSource 数据源
     * @return 数据库类型
     */
    public static DbType getDbType(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            if (connection == null) {
                throw new QueryFlowException("connection returned by DataSource ["
                    + dataSource + "] was null");
            }
            DatabaseMetaData metaData = connection.getMetaData();
            String jdbcUrl = metaData.getURL();
            return getDbType(jdbcUrl);
        } catch (SQLException e) {
            throw new QueryFlowException(e);
        } finally {
            close(connection);
        }
    }

    /**
     * 判断指定类型是否是 JDBC 支持的数据类型：
     * 基本数据类型及其包装类型，String，java.sql.Date，java.sql.Timestamp，java.sql.Time，BigDecimal，
     * byte[]，Blob，Clob
     *
     * @param type 类类型
     * @return 如果满足上述条件，返回 {@code true}
     */
    public static boolean isJdbcCommonClass(Type type) {
        Assert.notNull(type);

        if (!(type instanceof Class)) {
            return false;
        }
        Class<?> clazz = (Class<?>) type;
        return Utils.isPrimitiveOrWrapper(clazz)
            || String.class.equals(clazz)
            || java.sql.Date.class.equals(clazz)
            || java.sql.Timestamp.class.equals(clazz)
            || java.util.Date.class.equals(clazz)
            || java.sql.Time.class.equals(clazz)
            || BigDecimal.class.equals(clazz)
            || Number.class.equals(clazz)
            || byte[].class.equals(clazz)
            || Blob.class.equals(clazz)
            || Clob.class.equals(clazz);
    }

    /**
     * 通过指定的索引和数据类型从 ResultSet 中获取对应值
     *
     * @param rs        查询操作结果 ResultSet
     * @param index     列索引，从 1 开始
     * @param valueType 目标数据类型
     * @return 值
     * @throws SQLException 异常
     */
    public static Object getResultSetValue(ResultSet rs, int index, Class<?> valueType) throws SQLException {
        Object value;
        if (String.class.equals(valueType)) {
            return rs.getString(index);
        } else if (char.class.equals(valueType)) {
            String str = rs.getString(index);
            return str == null ? 0 : str.charAt(0);
        } else if (Character.class.equals(valueType)) {
            String str = rs.getString(index);
            return str == null ? null : str.charAt(0);
        } else if (int.class.equals(valueType)) {
            value = rs.getInt(index);
        } else if (Integer.class.equals(valueType)) {
            int result = rs.getInt(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (short.class.equals(valueType)) {
            value = rs.getShort(index);
        } else if (Short.class.equals(valueType)) {
            short result = rs.getShort(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (long.class.equals(valueType)) {
            value = rs.getLong(index);
        } else if (Long.class.equals(valueType)) {
            long result = rs.getLong(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (byte.class.equals(valueType)) {
            value = rs.getByte(index);
        } else if (Byte.class.equals(valueType)) {
            byte result = rs.getByte(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (float.class.equals(valueType)) {
            value = rs.getFloat(index);
        } else if (Float.class.equals(valueType)) {
            float result = rs.getFloat(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (double.class.equals(valueType)) {
            value = rs.getDouble(index);
        } else if (Double.class.equals(valueType) || Number.class.equals(valueType)) {
            double result = rs.getDouble(index);
            return result == 0 && rs.wasNull() ? null : result;
        } else if (boolean.class.equals(valueType)) {
            value = rs.getBoolean(index);
        } else if (Boolean.class.equals(valueType)) {
            boolean result = rs.getBoolean(index);
            return !result && rs.wasNull() ? null : result;
        } else if (BigDecimal.class.equals(valueType)) {
            return rs.getBigDecimal(index);
        } else if (BigInteger.class.equals(valueType)) {
            BigDecimal bigDecimal = rs.getBigDecimal(index);
            return bigDecimal == null ? null : bigDecimal.toBigInteger();
        } else if (Time.class.equals(valueType)) {
            return rs.getTime(index);
        } else if (Date.class.equals(valueType)) {
            return rs.getDate(index);
        } else if (java.util.Date.class.equals(valueType)) {
            Date sqlDate = rs.getDate(index);
            return sqlDate == null ? null : new java.util.Date(sqlDate.getTime());
        } else if (Timestamp.class.equals(valueType)) {
            return rs.getTimestamp(index);
        } else if (byte[].class.equals(valueType)) {
            return rs.getBytes(index);
        } else if (Byte[].class.equals(valueType)) {
            Blob blob = rs.getBlob(index);
            return blob == null ? null : Utils.toObjectArray(blob.getBytes(1, (int) blob.length()));
        } else if (Blob.class.equals(valueType)) {
            return rs.getBlob(index);
        } else if (Clob.class.equals(valueType)) {
            return rs.getClob(index);
        } else if (Reader.class.isAssignableFrom(valueType)) {
            Clob clob = rs.getClob(index);
            return clob == null ? null : clob.getCharacterStream();
        } else if (Instant.class.equals(valueType)) {
            Timestamp result = rs.getTimestamp(index);
            return result == null ? null : result.toInstant();
        } else if (Month.class.equals(valueType)) {
            int result = rs.getInt(index);
            return result == 0 && rs.wasNull() ? null : Month.of(result);
        } else if (YearMonth.class.equals(valueType)) {
            String result = rs.getString(index);
            return result == null ? null : YearMonth.parse(result);
        } else if (Year.class.equals(valueType)) {
            int result = rs.getInt(index);
            return result == 0 && rs.wasNull() ? null : Year.of(result);
        } else {
            if (getObjectWithTypeAvailable) {
                try {
                    return rs.getObject(index, valueType);
                } catch (AbstractMethodError err) {
                    log.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
                } catch (SQLFeatureNotSupportedException ex) {
                    log.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
                } catch (SQLException ex) {
                    log.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex);
                }
            }
            return getResultSetValue(rs, index);
        }
        return rs.wasNull() ? null : value;
    }

    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object value = rs.getObject(index);
        String className = null;
        if (value != null) {
            className = value.getClass().getName();
        }
        if (value instanceof Blob) {
            Blob blob = (Blob) value;
            value = blob.getBytes(1, (int) blob.length());
        } else if (value instanceof Clob) {
            Clob clob = (Clob) value;
            value = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            value = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String columnClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(columnClassName) || "oracle.sql.TIMESTAMP".equals(columnClassName)) {
                value = rs.getTimestamp(index);
            } else {
                value = rs.getDate(index);
            }
        } else if (value instanceof Date && "java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
            value = rs.getTimestamp(index);
        }
        return value;
    }

    /**
     * 获取指定位置的列名
     *
     * @param metaData ResultSet 元数据
     * @param index    列索引
     * @return 列名
     * @throws SQLException 异常
     */
    public static String getColumnName(ResultSetMetaData metaData, int index) throws SQLException {
        String columnName = metaData.getColumnLabel(index);
        if (Utils.isEmpty(columnName)) {
            columnName = metaData.getColumnName(index);
        }
        return columnName;
    }

}
