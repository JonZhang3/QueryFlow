package com.queryflow.accessor;

import com.alibaba.druid.pool.DruidDataSource;
import com.queryflow.common.QueryFlowException;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

class AccessorManager implements AccessorFactory {

    private static final Log LOG = LogFactory.getLog(AccessorManager.class);

    private final Map<String, Accessor> accessors = new LinkedHashMap<>();

    private AccessorManager() {
    }

    private static class InstanceHolder {
        static final AccessorManager INSTANCE = new AccessorManager();
    }

    public static AccessorManager manager() {
        return InstanceHolder.INSTANCE;
    }

    public static Accessor accessor() {
        return AccessorManager.manager().getAccessor();
    }

    public static Accessor accessor(int index) {
        return AccessorManager.manager().getAccessor(index);
    }

    public static Accessor accessor(String tag) {
        return AccessorManager.manager().getAccessor(tag);
    }

    public Accessor getAccessor() {
        Map.Entry<String, Accessor> headEntry =
            accessors.entrySet().iterator().next();
        return headEntry != null ? headEntry.getValue() : null;
    }

    public Accessor getAccessor(int index) {
        Collection<Accessor> values = accessors.values();
        int size = values.size();
        if (index > size) {
            throw new IndexOutOfBoundsException("the max index is " + (size - 1));
        }
        Iterator<Accessor> iterator = values.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            if (i == index) {
                return iterator.next();
            }
            i++;
        }
        throw new QueryFlowException("cannot found the accessor [" + index + "]");
    }

    public Accessor getAccessor(String tag) {
        if (Utils.isEmpty(tag)) {
            return getAccessor();
        }
        return accessors.get(tag);
    }

    private void addAccessor(String tag, Accessor accessor) {
        Assert.notEmpty(tag);
        Assert.notNull(accessor);

        if (accessors.containsKey(tag)) {
            throw new QueryFlowException("you have the same tag [" + tag + "] in the factory");
        }
        accessors.put(tag, accessor);
    }

    public int size() {
        return accessors.size();
    }

    public boolean containTag(String tag) {
        return accessors.containsKey(tag);
    }

    static void buildAccessor(DatabaseConfig config) {
        String tag = config.getTag();
        if (Utils.isEmpty(tag)) {
            tag = AccessorFactory.DEFAULT_TAG;
        }
        AccessorManager manager = AccessorManager.manager();
        manager.addAccessor(tag, createAccessor(createDatasource(config)));
    }

    static void buildAccessor(String tag, DataSource dataSource) {
        AccessorManager manager = AccessorManager.manager();
        if (Utils.isEmpty(tag)) {
            tag = AccessorFactory.DEFAULT_TAG;
        }
        manager.addAccessor(tag, createAccessor(dataSource));
    }

    private static Accessor createAccessor(DataSource dataSource) {
        return new DefaultAccessor(dataSource);
    }

    private static DataSource createDatasource(DatabaseConfig databaseConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(databaseConfig.getUrl());
        dataSource.setUsername(databaseConfig.getUsername());
        dataSource.setPassword(databaseConfig.getPassword());
        dataSource.setMaxActive(databaseConfig.getMaxActive());
        dataSource.setMinIdle(databaseConfig.getMinIdle());
        dataSource.setInitialSize(databaseConfig.getInitialSize());
        dataSource.setMaxWait(databaseConfig.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(databaseConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(databaseConfig.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(databaseConfig.getValidationQuery());
        dataSource.setValidationQueryTimeout(databaseConfig.getValidationQueryTimeout());
        dataSource.setPoolPreparedStatements(databaseConfig.isPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(databaseConfig.getMaxPoolPreparedStatementPerConnectionSize());
        if (databaseConfig.getConnectProperties() != null) {
            dataSource.setConnectProperties(databaseConfig.getConnectProperties());
        }
        try {
            if (Utils.isNotEmpty(databaseConfig.getFilters())) {
                dataSource.setFilters(databaseConfig.getFilters());
            }
        } catch (Exception e) {
            LOG.error("create druid datasource error:", e);
        }
        return dataSource;
    }

}
