package com.queryflow.config;

import com.queryflow.key.KeyGenerateUtil;
import com.queryflow.key.KeyGenerator;

/**
 * 全局配置。
 *
 * @author Jon
 * @since 0.5.0
 */
public class GlobalConfig {

    // 用于 Snowflake ID 生成算法
    private long workerId = 1;

    // 用于 Snowflake ID 生成算法
    private long dataCenterId = 1;

    // 是否将小驼峰命名法转化为下划线命名法
    // 将在实体映射，处理返回结果时起作用
    private boolean camelCaseToSnake = true;

    // 是否启用调试模式
    private boolean debug = false;

    // 默认的分页大小
    private int defaultPageLimit = 10;

    // 查询超时时间
    private int queryTimeout = 0;

    // 查询返回的最大行数
    private int queryMaxRows = 0;

    // 字段的最大大小
    private int maxFieldSize = 0;

    // 指定是否在每次执行后关闭数据库连接
    // false 表示不关闭，需要手动关闭
    private boolean closeAfterExecuted = true;

    private GlobalConfig() {
    }

    private static class InstanceHolder {
        private static final GlobalConfig INSTANCE = new GlobalConfig();
    }

    public static GlobalConfig configer() {
        return InstanceHolder.INSTANCE;
    }

    public static long getWorkerId() {
        return configer().workerId;
    }

    public static long getDataCenterId() {
        return configer().dataCenterId;
    }

    public static boolean isCamelCaseToSnake() {
        return configer().camelCaseToSnake;
    }

    public static boolean isDebug() {
        return configer().debug;
    }

    public static int getDefaultPageLimit() {
        return configer().defaultPageLimit;
    }

    public static int getQueryTimeout() {
        return configer().queryTimeout;
    }

    public static int getQueryMaxRows() {
        return configer().queryMaxRows;
    }

    public static int getMaxFieldSize() {
        return configer().maxFieldSize;
    }

    public static boolean isCloseAfterExecuted() {
        return configer().closeAfterExecuted;
    }

    public GlobalConfig setWorkerId(long id) {
        if (id >= 0) {
            workerId = id;
        }
        return this;
    }

    public GlobalConfig setDataCenterId(long id) {
        if (id > 0) {
            dataCenterId = id;
        }
        return this;
    }

    public GlobalConfig camelCaseToSnake(boolean convert) {
        this.camelCaseToSnake = convert;
        return this;
    }

    /**
     * Registers the implementation class of {@code KeyGenerator} by yourself.
     *
     * @param clazz the implementation class of {@code KeyGenerator}, must not null
     * @return GlobalConfig
     */
    public GlobalConfig registerKeyGenerator(Class<? extends KeyGenerator<?>> clazz) {
        KeyGenerateUtil.registerKeyGenerator(clazz);
        return this;
    }

    public GlobalConfig debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public GlobalConfig defaultPageLimit(int defaultPageLimit) {
        if (defaultPageLimit > 0) {
            this.defaultPageLimit = defaultPageLimit;
        }
        return this;
    }

    /**
     * 为 {@code Statement} 中 {@code execute}，{@code executeUpdate}，
     * {@code executeQuery} 设置执行超时时间
     *
     * @param seconds 操作执行超时时间。如果设置为 {@code 0}，表示没有限制。单位：秒
     * @see java.sql.Statement#setQueryTimeout(int)
     * @return GlobalConfig
     */
    public GlobalConfig queryTimeout(int seconds) {
        if (seconds >= 0) {
            this.queryTimeout = seconds;
        }
        return this;
    }

    /**
     * 为 {@code Statement} 查询操作生成的 {@code ResultSet} 设置最大行数限制。
     * 如果超出了该限制，剩余的行数将被删除。
     *
     * @param max 最大行数。如果设置为 {@code 0}，表示没有限制
     * @see java.sql.Statement#setMaxRows(int)
     * @return GlobalConfig
     */
    public GlobalConfig queryMaxRows(int max) {
        if (max >= 0) {
            this.queryMaxRows = max;
        }
        return this;
    }

    /**
     * 设置 {@code ResultSet} 中可返回的字符或字节列的最大量限制。
     * 该限制仅仅可被设置在 <code>BINARY</code>，<code>VARBINARY</code>，
     * <code>LONGVARBINARY</code>，<code>CHAR</code>，<code>VARCHAR</code>，
     * <code>NCHAR</code>，<code>NVARCHAR</code>，<code>LONGNVARCHAR</code>，
     * <code>LONGVARCHAR</code> 类型的字段上。
     * 如果超出该限制，多余的数据将被丢弃。
     * 为了最大限度的实现可移植，值要尽可能大于 256。
     *
     * @param max 最大的字节量。如果设置为 {@code 0}，表示没有限制
     * @return GlobalConfig
     */
    public GlobalConfig maxFieldSize(int max) {
        if (max >= 0) {
            this.maxFieldSize = max;
        }
        return this;
    }

    public GlobalConfig closeAfterExecuted(boolean close) {
        this.closeAfterExecuted = close;
        return this;
    }


}
