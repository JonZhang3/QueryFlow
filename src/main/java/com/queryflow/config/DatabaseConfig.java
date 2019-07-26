package com.queryflow.config;

import java.util.Properties;

import com.alibaba.druid.pool.DruidAbstractDataSource;

/**
 * 数据源配置。所有的配置项对照 Druid 中的配置
 *
 * @author Jon
 * @since 1.0.0
 */
public class DatabaseConfig {

    private String tag;
    private String url;
    private String username;
    private String password;
    private int maxActive = DruidAbstractDataSource.DEFAULT_MAX_ACTIVE_SIZE;
    private int initialSize = DruidAbstractDataSource.DEFAULT_INITIAL_SIZE;
    private int minIdle = DruidAbstractDataSource.DEFAULT_MIN_IDLE;
    private long maxWait = DruidAbstractDataSource.DEFAULT_MAX_WAIT;// 获取连接等待超时时间, ms
    private long timeBetweenEvictionRunsMillis
        = DruidAbstractDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;// 间隔多久进行一次检测，检测需要关闭的空闲连接， ms
    private long minEvictableIdleTimeMillis =
        DruidAbstractDataSource.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;// 一个连接在池中最小生存时间，ms
    private String validationQuery = DruidAbstractDataSource.DEFAULT_VALIDATION_QUERY;
    private int validationQueryTimeout = 0;
    private boolean poolPreparedStatements = false;// 是否打开 PSCache
    private int maxPoolPreparedStatementPerConnectionSize = 10;// 指定每个连接上 PSCache 的大小
    private String filters;// 配置拦截器
    private Properties connectProperties;

    public DatabaseConfig() {
    }

    public DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getTag() {
        return tag;
    }

    public DatabaseConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DatabaseConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DatabaseConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DatabaseConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public DatabaseConfig setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        return this;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public DatabaseConfig setInitialSize(int initialSize) {
        this.initialSize = initialSize;
        return this;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public DatabaseConfig setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public DatabaseConfig setMaxWait(long maxWait) {
        this.maxWait = maxWait;
        return this;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public DatabaseConfig setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        return this;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public DatabaseConfig setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        return this;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public DatabaseConfig setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
        return this;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public DatabaseConfig setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
        return this;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public DatabaseConfig setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        return this;
    }

    public String getFilters() {
        return filters;
    }

    public DatabaseConfig setFilters(String filters) {
        this.filters = filters;
        return this;
    }

    public Properties getConnectProperties() {
        return connectProperties;
    }

    public DatabaseConfig setConnectProperties(Properties connectProperties) {
        this.connectProperties = connectProperties;
        return this;
    }

    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public DatabaseConfig setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
        return this;
    }

}
