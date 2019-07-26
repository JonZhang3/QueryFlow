package com.queryflow.mapper;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.JdbcUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MapperMethodParameter {

    private String name;
    private Type genericType;
    private int index;
    private ParameterType parameterType;

    public MapperMethodParameter(String name, Type genericType, int index) {
        this.name = name;
        this.index = index;
        this.genericType = genericType;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type rawType = parameterizedType.getRawType();
            if (Map.class.isAssignableFrom((Class<?>) rawType)) {
                parameterType = ParameterType.MAP;
            } else {
                throw new QueryFlowException("the mapper method parameters must be the jdbc common type or the map type");
            }
        } else if (genericType instanceof Class) {
            if (Map.class.isAssignableFrom((Class<?>) genericType)) {
                parameterType = ParameterType.MAP;
            } else if (List.class.isAssignableFrom((Class<?>) genericType)) {
                throw new QueryFlowException("not support List type in the mapper method parameters");
            } else if (JdbcUtil.isJdbcCommonClass(genericType)) {
                parameterType = ParameterType.COMMON;
            } else {
                parameterType = ParameterType.BEAN;
            }
        }
    }

    public Type getType() {
        return genericType;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public ParameterType getParameterType() {
        return this.parameterType;
    }

    public enum ParameterType {
        MAP,
        BEAN,
        COMMON
    }

}
