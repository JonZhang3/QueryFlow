package com.queryflow.common;

import java.sql.Types;

public enum DataType {

    BIT(Types.BIT),// boolean or Boolean
    TINYINT(Types.TINYINT),// byte or Byte
    SMALLINT(Types.SMALLINT),// short or Short
    INTEGER(Types.INTEGER),// int or Integer
    BIGINT(Types.BIGINT),// long or Long
    FLOAT(Types.FLOAT),// float or Float
    REAL(Types.REAL),
    DOUBLE(Types.DOUBLE),// double or Double
    NUMERIC(Types.NUMERIC),// BigDecimal
    DECIMAL(Types.DECIMAL),// BigDecimal
    CHAR(Types.CHAR),// String
    VARCHAR(Types.VARCHAR),// String
    LONGVARCHAR(Types.LONGVARCHAR),// String
    DATE(Types.DATE),// java.util.Date or java.sql.Date
    TIME(Types.TIME),// java.util.Date or java.sql.Time
    TIMESTAMP(Types.TIMESTAMP),// java.util.Date or java.sql.Timestamp
    BINARY(Types.BINARY),// byte[]
    VARBINARY(Types.VARBINARY),// byte[]
    LONGVARBINARY(Types.LONGVARBINARY),// byte[]
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    JAVA_OBJECT(Types.JAVA_OBJECT),
    DISTINCT(Types.DISTINCT),
    STRUCT(Types.STRUCT),
    ARRAY(Types.ARRAY),
    BLOB(Types.BLOB),// byte[]
    CLOB(Types.CLOB),// String
    REF(Types.REF),
    DATALINK(Types.DATALINK),
    BOOLEAN(Types.BOOLEAN),// byte or Byte or boolean
    ROWID(Types.ROWID),
    NCHAR(Types.NCHAR),// String
    NVARCHAR(Types.NVARCHAR),// String
    LONGNVARCHAR(Types.LONGNVARCHAR),// String
    NCLOB(Types.NCLOB),// String
    SQLXML(Types.SQLXML);

    private int value;

    DataType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
