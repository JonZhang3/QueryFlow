package com.queryflow.accessor;

import com.queryflow.common.QueryFlowException;
import com.queryflow.annotation.Mapper;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.config.parser.ConfigFileFactory;
import com.queryflow.mapper.MapperManager;
import com.queryflow.utils.Assert;
import com.queryflow.utils.ClassScanner;
import com.queryflow.utils.Utils;

import javax.sql.DataSource;
import java.util.*;

/**
 * 创建数据库 API，一般在程序启动初始化时使用。
 * 使用 {@code addDatabase} 方法添加数据库连接（可多数据源），最后调用
 * {@code build} 方法创建数据库连接。
 */
public class AccessorFactoryBuilder {

    private List<DatabaseConfig> databaseConfigs = new ArrayList<>(2);
    private Map<String, DataSource> dataSources = new HashMap<>();
    private List<String> scanPackageNames = new ArrayList<>(2);

    public AccessorFactoryBuilder fromFile() {
        fromFile(null);
        return this;
    }

    public AccessorFactoryBuilder fromFile(String path) {
        List<DatabaseConfig> dbConfigs = ConfigFileFactory.parseConfigFile(path);
        for (DatabaseConfig databaseConfig : dbConfigs) {
            addDatabase(databaseConfig);
        }
        return this;
    }

    public AccessorFactoryBuilder addDatabase(DataSource dataSource) {
        Assert.notNull(dataSource);

        dataSources.put(AccessorFactory.DEFAULT_TAG, dataSource);
        return this;
    }

    public AccessorFactoryBuilder addDatabase(String tag, DataSource dataSource) {
        Assert.hasText(tag);
        Assert.notNull(dataSource);

        dataSources.put(tag, dataSource);
        return this;
    }

    public AccessorFactoryBuilder addDatabase(DatabaseConfig config) {
        Assert.notNull(config);

        databaseConfigs.add(config);
        return this;
    }

    public AccessorFactoryBuilder addDatabase(String url, String username, String password) {
        return addDatabse(AccessorFactory.DEFAULT_TAG, url, username, password);
    }

    public AccessorFactoryBuilder addDatabse(String tag, String url, String username, String password) {
        Assert.hasText(url);
        Assert.hasText(username);
        Assert.hasText(password);

        databaseConfigs.add(new DatabaseConfig(url, username, password).setTag(tag));
        return this;
    }

    public AccessorFactoryBuilder scanPackage(String... packageNames) {
        if (packageNames != null && packageNames.length > 0) {
            for (String packageName : packageNames) {
                if (!Utils.isBlank(packageName)) {
                    this.scanPackageNames.add(packageName);
                }
            }
        }
        return this;
    }

    public AccessorFactory build() {
        return build(true);
    }

    public AccessorFactory build(boolean scanPackage) {
        if (databaseConfigs.isEmpty()) {
            throw new QueryFlowException("you must configure a databse source");
        }
        buildAccessors();
        if (scanPackage) {
            buildPackage();
        }
        return AccessorManager.manager();
    }

    private void buildAccessors() {
        for (DatabaseConfig config : databaseConfigs) {
            if (config != null) {
                AccessorManager.buildAccessor(config);
            }
        }
        Set<Map.Entry<String, DataSource>> entries = dataSources.entrySet();
        for (Map.Entry<String, DataSource> entry : entries) {
            AccessorManager.buildAccessor(entry.getKey(), entry.getValue());
        }
    }

    private void buildPackage() {
        ClassScanner
            .newScanner(toStringArray(scanPackageNames))
            .setFilter(clazz -> {
                if (clazz.getAnnotation(Mapper.class) != null) {
                    MapperManager.addMapperClass(clazz);
                }
                return false;
            }).scan();
    }

    private String[] toStringArray(List<String> values) {
        if (values == null) {
            return null;
        }
        String[] strings = new String[values.size()];
        return values.toArray(strings);
    }

}
