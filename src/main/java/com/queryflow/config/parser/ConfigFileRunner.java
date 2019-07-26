package com.queryflow.config.parser;

import com.queryflow.config.DatabaseConfig;
import com.queryflow.config.GlobalConfig;
import com.queryflow.reflection.Reflector;
import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.utils.Converts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 将从配置文件解析到的内容进行封装，方便其他功能使用
 *
 * @author Jon
 * @since 1.0.0
 */
class ConfigFileRunner {

    List<DatabaseConfig> run(Map<String, Object> config) {
        // 全局配置
        GlobalConfig globalConfig = GlobalConfig.configer();
        globalConfig.setWorkerId(Converts.toLong(config.get("workerId"), 1L));
        globalConfig.setDataCenterId(Converts.toLong(config.get("dataCenterId"), 1L));
        globalConfig.camelCaseToSnake(Converts.toBoolean(config.get("camelCaseToSnake"), true));
        globalConfig.debug(Converts.toBoolean(config.get("debug"), false));
        globalConfig.defaultPageLimit(Converts.toInteger(config.get("defaultPageLimit"), 10));
        globalConfig.queryTimeout(Converts.toInteger(config.get("queryTimeout"), 0));
        globalConfig.queryMaxRows(Converts.toInteger(config.get("queryMaxRows"), 0));
        globalConfig.maxFieldSize(Converts.toInteger(config.get("maxFieldSize"), 0));
        globalConfig.closeAfterExecuted(Converts.toBoolean(config.get("closeAfterExecuted"), false));

        // 数据源配置
        List<DatabaseConfig> databaseConfigs = new LinkedList<>();
        Reflector reflector = new Reflector(DatabaseConfig.class);
        databaseConfigs.add(initDatabaseConfig(reflector, config));

        Object datasourcesObj = config.get("datasources");
        if (datasourcesObj != null) {
            List<Map<String, Object>> datasources = (List<Map<String, Object>>) datasourcesObj;
            for (Map<String, Object> datasource : datasources) {
                databaseConfigs.add(initDatabaseConfig(reflector, datasource));
            }
        }
        return databaseConfigs;
    }

    private DatabaseConfig initDatabaseConfig(Reflector reflector, Map<String, Object> config) {
        DatabaseConfig databaseConfig = (DatabaseConfig) reflector.newInstance();
        Iterator<FieldInvoker> fieldIterator = reflector.fieldIterator();
        while (fieldIterator.hasNext()) {
            FieldInvoker invoker = fieldIterator.next();
            String fieldName = invoker.getName();
            Object value = config.get(fieldName);
            if (value != null) {
                invoker.setValue(databaseConfig, Converts.to(invoker.getType(), value));
            }
        }
        return databaseConfig;
    }

}
