package com.queryflow.accessor.statement;

import com.queryflow.accessor.runner.OutParameter;
import com.queryflow.utils.Converts;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class OutParameters {

    private Map<String, OutParameter> parameters = new LinkedHashMap<>();
    int updateRows = 0;

    public Collection<OutParameter> getAllParameters() {
        return parameters.values();
    }

    public void putParameter(String name, OutParameter outParameter) {
        parameters.put(name, outParameter);
    }

    public OutParameter getParameter(String name) {
        return parameters.get(name);
    }

    public int getUpdateRows() {
        return updateRows;
    }

    public Object getObject(String name) {
        OutParameter parameter = getParameter(name);
        if(parameter != null) {
            return parameter.getValue();
        }
        return null;
    }

    public String getString(String name) {
        return Converts.toStr(getObject(name), null);
    }

    public String getString(String name, String defaultValue) {
        return Converts.toStr(getObject(name), defaultValue);
    }

    public Character getCharacter(String name) {
        return Converts.toCharacter(getObject(name), null);
    }

    public Character getCharacter(String name, Character defaultValue) {
        return Converts.toCharacter(getObject(name), defaultValue);
    }

    public Boolean getBoolean(String name) {
        return Converts.toBoolean(getObject(name), null);
    }

    public Boolean getBoolean(String name, Boolean defaultValue) {
        return Converts.toBoolean(getObject(name), defaultValue);
    }

    public Byte getByte(String name) {
        return Converts.toByte(getObject(name), null);
    }

    public Byte getByte(String name, Byte defaultValue) {
        return Converts.toByte(getObject(name), defaultValue);
    }

    public Short getShort(String name) {
        return Converts.toShort(getObject(name), null);
    }

    public Short getShort(String name, Short defaultValue) {
        return Converts.toShort(getObject(name), defaultValue);
    }

    public Integer getInteger(String name) {
        return Converts.toInteger(getObject(name), null);
    }

    public Integer getInteger(String name, Integer defaultValue) {
        return Converts.toInteger(getObject(name), defaultValue);
    }

    public Long getLong(String name) {
        return Converts.toLong(getObject(name), null);
    }

    public Long getLong(String name, Long defaultValue) {
        return Converts.toLong(getObject(name), defaultValue);
    }

    public Float getFloat(String name) {
        return Converts.toFloat(getObject(name), null);
    }

    public Float getFloat(String name, Float defaultValue) {
        return Converts.toFloat(getObject(name), defaultValue);
    }

    public Double getDouble(String name) {
        return Converts.toDouble(getObject(name), null);
    }

    public Double getDouble(String name, Double defaultValue) {
        return Converts.toDouble(getObject(name), defaultValue);
    }

    public byte[] getBytes(String name) {
        return Converts.toBytes(getObject(name), new byte[0]);
    }

    public byte[] getBytes(String name, byte[] defaultValue) {
        return Converts.toBytes(getObject(name), defaultValue);
    }

    public BigDecimal getBigDecimal(String name) {
        return Converts.toBigDecimal(getObject(name), null);
    }

    public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
        return Converts.toBigDecimal(getObject(name), defaultValue);
    }

    public java.sql.Date getDate(String name) {
        return Converts.toDate(getObject(name), null);
    }

    public java.sql.Date getDate(String name, java.sql.Date defaultValue) {
        return Converts.toDate(getObject(name), defaultValue);
    }

    public Time getTime(String name) {
        return Converts.toTime(getObject(name), null);
    }

    public Time getTime(String name, Time defaultValue) {
        return Converts.toTime(getObject(name), defaultValue);
    }

    public Timestamp getTimestamp(String name) {
        return Converts.toTimestamp(getObject(name), null);
    }

    public Timestamp getTimestamp(String name, Timestamp defaultValue) {
        return Converts.toTimestamp(getObject(name), defaultValue);
    }

    public java.util.Date getUtilDate(String name, String pattern) {
        return Converts.toUtilDate(getObject(name), pattern, null);
    }

    public java.util.Date getUtilDate(String name, String pattern, java.util.Date defaultValue) {
        return Converts.toUtilDate(getObject(name), pattern, defaultValue);
    }

    public Clob getClob(String name) {
        return Converts.toClob(getObject(name), null);
    }

    public Clob getClob(String name, Clob defaultValue) {
        return Converts.toClob(getObject(name), defaultValue);
    }

    public Blob getBlob(String name) {
        return Converts.toBlob(getObject(name), null);
    }

    public Blob getBlob(String name, Blob defaultValue) {
        return Converts.toBlob(getObject(name), defaultValue);
    }

}
