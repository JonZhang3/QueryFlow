package com.queryflow.page;

import com.queryflow.common.DbType;
import com.queryflow.utils.Utils;

import java.util.Map;
import java.util.HashMap;

public final class SimplePageSqlProcessSelector implements PageSqlProcessSelector {

    private Map<String, PageSqlMatchProcess> processes = new HashMap<>();
    private Map<String, Class<? extends PageSqlMatchProcess>> processClasses = new HashMap<>();

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
        PageSqlMatchProcess process = processes.get(dbType);
        if (process == null) {
            synchronized (this) {
                process = processes.get(dbType);
                if (process == null) {
                    Class<? extends PageSqlMatchProcess> processClass = processClasses.get(dbType);
                    if (processClass == null) {
                        throw new RuntimeException(String.format(
                            "根据数据库类型:%s,获取不到相应的PageSqlMatchProcess分页处理器", dbType));
                    } else {
                        process = Utils.instantiate(processClass);
                    }
                    processes.put(dbType, process);
                }
            }
        }
        return process;
    }

    public static void main(String[] args) {
        PageSqlProcessSelector selector = new SimplePageSqlProcessSelector();
        selector.select(DbType.MYSQL.value());
    }

}
