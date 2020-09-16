package com.queryflow.common;

public enum DbType {

    MYSQL("mysql"),
    ORACLE("Oracle"),
    DB2("db2"),
    H2("H2"),
    HSQL("hsql"),
    INFORMIX("Informix"),
    DERBY("derby"),
    SQLITE("sqlite"),
    POSTGRESQL("postgresql"),
    SQLSERVER2005("sqlserver2005"),
    SQLSERVER("sqlserver"),
    MARIADB("mariadb"),
    OTHER("other");

    private final String value;

    DbType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
