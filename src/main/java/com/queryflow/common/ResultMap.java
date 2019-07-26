package com.queryflow.common;

import com.queryflow.utils.Converts;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;

public class ResultMap extends LinkedHashMap<String, Object> {

    public ResultMap() {
        super();
    }

    public ResultMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ResultMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public String getString(String name) {
        return Converts.toStr(get(name), null);
    }

    public String getString(String name, String defaultValue) {
        return Converts.toStr(get(name), defaultValue);
    }

    public Character getCharacter(String name) {
        return Converts.toCharacter(get(name), null);
    }

    public Character getCharacter(String name, Character defaultValue) {
        return Converts.toCharacter(get(name), defaultValue);
    }

    public Boolean getBoolean(String name) {
        return Converts.toBoolean(get(name), null);
    }

    public Boolean getBoolean(String name, Boolean defaultValue) {
        return Converts.toBoolean(get(name), defaultValue);
    }

    public Byte getByte(String name) {
        return Converts.toByte(get(name), null);
    }

    public Byte getByte(String name, Byte defaultValue) {
        return Converts.toByte(get(name), defaultValue);
    }

    public Short getShort(String name) {
        return Converts.toShort(get(name), null);
    }

    public Short getShort(String name, Short defaultValue) {
        return Converts.toShort(get(name), defaultValue);
    }

    public Integer getInteger(String name) {
        return Converts.toInteger(get(name), null);
    }

    public Integer getInteger(String name, Integer defaultValue) {
        return Converts.toInteger(get(name), defaultValue);
    }

    public Long getLong(String name) {
        return Converts.toLong(get(name), null);
    }

    public Long getLong(String name, Long defaultValue) {
        return Converts.toLong(get(name), defaultValue);
    }

    public Float getFloat(String name) {
        return Converts.toFloat(get(name), null);
    }

    public Float getFloat(String name, Float defaultValue) {
        return Converts.toFloat(get(name), defaultValue);
    }

    public Double getDouble(String name) {
        return Converts.toDouble(get(name), null);
    }

    public Double getDouble(String name, Double defaultValue) {
        return Converts.toDouble(get(name), defaultValue);
    }

    public byte[] getBytes(String name) {
        return Converts.toBytes(get(name), new byte[0]);
    }

    public byte[] getBytes(String name, byte[] defaultValue) {
        return Converts.toBytes(get(name), defaultValue);
    }

    public BigDecimal getBigDecimal(String name) {
        return Converts.toBigDecimal(get(name), null);
    }

    public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
        return Converts.toBigDecimal(get(name), defaultValue);
    }

    public Date getDate(String name) {
        return Converts.toDate(get(name), null);
    }

    public Date getDate(String name, Date defaultValue) {
        return Converts.toDate(get(name), defaultValue);
    }

    public Time getTime(String name) {
        return Converts.toTime(get(name), null);
    }

    public Time getTime(String name, Time defaultValue) {
        return Converts.toTime(get(name), defaultValue);
    }

    public Timestamp getTimestamp(String name) {
        return Converts.toTimestamp(get(name), null);
    }

    public Timestamp getTimestamp(String name, Timestamp defaultValue) {
        return Converts.toTimestamp(get(name), defaultValue);
    }

    public java.util.Date getUtilDate(String name, String pattern) {
        return Converts.toUtilDate(get(name), pattern, null);
    }

    public java.util.Date getUtilDate(String name, String pattern, java.util.Date defaultValue) {
        return Converts.toUtilDate(get(name), pattern, defaultValue);
    }

    public Clob getClob(String name) {
        return Converts.toClob(get(name), null);
    }

    public Clob getClob(String name, Clob defaultValue) {
        return Converts.toClob(get(name), defaultValue);
    }

    public Blob getBlob(String name) {
        return Converts.toBlob(get(name), null);
    }

    public Blob getBlob(String name, Blob defaultValue) {
        return Converts.toBlob(get(name), defaultValue);
    }

}
