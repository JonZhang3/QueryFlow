package com.queryflow.page;

import com.queryflow.common.DbType;
import com.queryflow.common.QueryFlowException;
import com.queryflow.page.impl.*;
import com.queryflow.utils.Utils;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class SimplePageSqlProcessSelector implements PageSqlProcessSelector {

    private final Map<String, PageSqlMatchProcess> processes = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends PageSqlMatchProcess>> processClasses = new HashMap<>();

    public SimplePageSqlProcessSelector() {
        processClasses.put(DbType.DERBY.value(), DerbyPageSqlMatchProcess.class);
        processClasses.put(DbType.H2.value(), H2PageSqlMatchProcess.class);
        processClasses.put(DbType.MYSQL.value(), MysqlPageSqlMatchProcess.class);
        processClasses.put(DbType.ORACLE.value(), OraclePageSqlMatchProcess.class);
        processClasses.put(DbType.SQLITE.value(), SQLitePageSqlMatchProcess.class);
        processClasses.put(DbType.SQLSERVER.value(), SqlServerPageSqlMatchProcess.class);
    }

    @Override
    public PageSqlMatchProcess select(String dbType) {
        PageSqlMatchProcess process = processes.computeIfAbsent(dbType, type -> {
            Class<? extends PageSqlMatchProcess> clazz = processClasses.get(type);
            if (clazz != null) {
                return Utils.instantiate(clazz);
            }
            return null;
        });
        if (process == null) {
            throw new QueryFlowException(String.format(
                "根据数据库类型:%s,获取不到相应的PageSqlMatchProcess分页处理器", dbType));
        }
        return process;
    }

}
