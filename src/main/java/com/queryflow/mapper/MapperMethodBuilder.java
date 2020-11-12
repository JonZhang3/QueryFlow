package com.queryflow.mapper;

import com.queryflow.common.QueryFlowException;
import com.queryflow.annotation.*;
import com.queryflow.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapperMethodBuilder {

    private static final Map<Method, MapperMethod> CONTAINER
        = new ConcurrentHashMap<>();

    private static final String ERROR_RETURN_MESSAGE = "the return type of mapper methd supports List, Map, Jdbc Common class or bean object";

    private static final Pattern NAMED_PATTERN
        = Pattern.compile("(\\$\\{([^{}]+)\\})");

    public static MapperMethod getMapperMethod(final Method method) {
        MapperMethod mapperMethod = CONTAINER.get(method);
        if (mapperMethod == null) {
            synchronized (MapperMethodBuilder.class) {
                mapperMethod = CONTAINER.get(method);
                if (mapperMethod == null) {
                    mapperMethod = new MapperMethodBuilder(method).build();
                }
            }
        }
        return mapperMethod;
    }

    private final Method method;
    private final String methodDesc;
    private MapperMethod.SqlType sqlType;

    public MapperMethodBuilder(Method method) {
        this.method = method;
        Class<?> declaringClass = method.getDeclaringClass();
        methodDesc = declaringClass.getName() + " " + method.getName();
    }

    public MapperMethod build() {
        MapperMethod mapperMethod = CONTAINER.get(method);
        if (mapperMethod != null) {
            return mapperMethod;
        } else {
            if (Modifier.isAbstract(method.getModifiers())) {
                Select select = method.getAnnotation(Select.class);
                Update update = method.getAnnotation(Update.class);
                String rawSql;
                if (select != null) {
                    this.sqlType = MapperMethod.SqlType.QUERY;
                    mapperMethod = new MapperMethod(MapperMethod.SqlType.QUERY);
                    rawSql = select.value();
                } else if (update != null) {
                    this.sqlType = MapperMethod.SqlType.EXECUTE;
                    mapperMethod = new MapperMethod(MapperMethod.SqlType.EXECUTE);
                    rawSql = update.value();
                } else {
                    throw new QueryFlowException(methodDesc, "the method is abstract, but is not a mapper method");
                }
                if (Utils.isBlank(rawSql)) {
                    throw new QueryFlowException(methodDesc, "you must specify a sql for the mapper method");
                }

                DataSource dataSource = method.getAnnotation(DataSource.class);
                if (dataSource != null) {
                    String[] sourceTags = dataSource.value();
                    if(sourceTags.length == 0) {
                        mapperMethod.setDataSourceTag("");
                    } else {
                        mapperMethod.setDataSourceTag(sourceTags[0]);
                    }
                }
                List<String> names = parseSql(rawSql, mapperMethod);
                Map<String, MapperMethodParameter> parameterMap = parseParameter();
                paramterToSqlName(names, parameterMap, mapperMethod);
                parseReturnType(mapperMethod);
                mapperMethod.buildExecutor();
                CONTAINER.put(method, mapperMethod);
            }
            return mapperMethod;
        }
    }

    private List<String> parseSql(String rawSql, MapperMethod mapperMethod) {
        StringBuffer result = new StringBuffer(rawSql.length());

        Matcher matcher = NAMED_PATTERN.matcher(rawSql);
        List<String> names = new ArrayList<>();
        String name;
        while (matcher.find()) {
            name = matcher.group(2);
            if (Utils.isEmpty(name)) {
                throw new QueryFlowException(methodDesc, "you muse specify a name in the sql interpolations");
            }
            names.add(name);
            matcher.appendReplacement(result, "?");
        }
        matcher.appendTail(result);
        mapperMethod.setPreparedSql(result.toString());
        return names;
    }

    private Map<String, MapperMethodParameter> parseParameter() {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Map<String, MapperMethodParameter> parameterMap = new HashMap<>();
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            MapperMethodParameter parameter;
            Type parameterType;
            for (int i = 0, len = parameterTypes.length; i < len; i++) {
                parameterType = parameterTypes[i];
                Annotation[] paramterAnnos = parameterAnnotations[i];
                if (paramterAnnos != null && paramterAnnos.length > 0) {
                    for (Annotation anno : paramterAnnos) {
                        if (anno.annotationType().equals(Bind.class)) {
                            Bind bind = (Bind) anno;
                            parameter = new MapperMethodParameter(
                                bind.value(), parameterType, i
                            );
                            parameterMap.put(bind.value(), parameter);
                            break;
                        }
                    }
                }
            }
        }
        return parameterMap;
    }

    private void paramterToSqlName(List<String> names,
                                   Map<String, MapperMethodParameter> parameters,
                                   MapperMethod mapperMethod) {
        if (names.isEmpty()) {
            return;
        }
        if (parameters.isEmpty()) {
            throw new QueryFlowException(methodDesc, "you must specify mapper mathod parameters");
        }
        MapperMethodParameter parameter;
        String name;
        String childName = "";
        for (int i = 0, len = names.size(); i < len; i++) {
            name = names.get(i);
            if (name.contains(".")) {
                String[] splitNames = name.split("\\.");
                if (splitNames.length == 1 || splitNames.length > 2) {
                    throw new QueryFlowException(methodDesc, "only one dot is allowed in the sql placeholder");
                }
                parameter = parameters.get(splitNames[0]);
                childName = splitNames[1];
            } else {
                parameter = parameters.get(name);
            }
            if (parameter == null) {
                throw new QueryFlowException(methodDesc, "cound found the method paramter by the name: " + name);
            }
            SqlValue sqlValue = new SqlValue();
            switch (parameter.getParameterType()) {
                case MAP:
                    sqlValue.setType(SqlValue.Type.MAP_VALUE);
                    sqlValue.setName(childName);
                    break;
                case BEAN:
                    sqlValue.setType(SqlValue.Type.BEAN_VALUE);
                    sqlValue.setName(childName);
                    sqlValue.setBeanClass((Class<?>) parameter.getType());
                    break;
                case COMMON:
                    sqlValue.setType(SqlValue.Type.VALUE);
                    sqlValue.setName(name);
                    break;
            }
            sqlValue.setIndex(parameter.getIndex());
            mapperMethod.addSqlValue(sqlValue);
        }
    }

    private void parseReturnType(MapperMethod mapperMethod) {
        if (sqlType == MapperMethod.SqlType.EXECUTE) {
            return;
        }
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof Class) {
            if (List.class.equals(genericReturnType)) {
                throw new QueryFlowException("");
            } else if (Map.class.equals(genericReturnType)) {
                mapperMethod.setReturnType(MapperMethod.ReturnType.MAP);
            } else {
                mapperMethod.setReturnType(MapperMethod.ReturnType.BEAN);
            }
            mapperMethod.setReturnClass((Class<?>) genericReturnType);
        } else if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (List.class.equals(rawType)) {
                Type actualType = parameterizedType.getActualTypeArguments()[0];
                if (actualType instanceof Class) {
                    Class<?> actualClass = (Class<?>) actualType;
                    if (List.class.isAssignableFrom(actualClass)) {
                        throw new QueryFlowException(methodDesc, "the mapper method returns a List, but the generics of list not support List");
                    } else if (Map.class.equals(actualClass)) {
                        mapperMethod.setReturnType(MapperMethod.ReturnType.LIST_MAP);
                    } else {
                        mapperMethod.setReturnType(MapperMethod.ReturnType.LIST_BEAN);
                    }
                    mapperMethod.setReturnClass(actualClass);
                } else if (actualType instanceof ParameterizedType) {
                    ParameterizedType secondParameterizedTye = (ParameterizedType) actualType;
                    if (!Map.class.equals(secondParameterizedTye.getOwnerType())) {
                        throw new QueryFlowException(methodDesc, ERROR_RETURN_MESSAGE);
                    }
                    mapperMethod.setReturnClass(Map.class);
                    mapperMethod.setReturnType(MapperMethod.ReturnType.LIST_MAP);
                }
            } else if (Map.class.equals(rawType)) {
                mapperMethod.setReturnType(MapperMethod.ReturnType.MAP);
                mapperMethod.setReturnClass(Map.class);
            } else {
                throw new QueryFlowException(methodDesc, ERROR_RETURN_MESSAGE);
            }
        }
    }

}
