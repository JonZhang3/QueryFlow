package com.queryflow.accessor.handler;

import com.queryflow.cache.impl.LFUCache;
import com.queryflow.config.GlobalConfig;
import com.queryflow.reflection.ReflectionUtil;
import com.queryflow.reflection.Reflector;
import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.utils.JdbcUtil;
import com.queryflow.utils.Utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 将查询结果中的一行处理成 Java Bean 类。
 * 同时支持基本数据类型及其包装类的转化。
 *
 * @author Jon
 * @since 1.0.0
 */
public class BeanResultSetHandler<T> implements ResultSetHandler {

    // Java Bean 反射缓存
    private static final LFUCache<Class<?>, BeanResultSetHandler>
        BEAN_HANDLER_CACHE = new LFUCache<>(20);

    @SuppressWarnings("unchecked")
    public static <R> BeanResultSetHandler<R> newBeanHandler(Class<R> requiredType) {
        if (requiredType == null) {
            throw new NullPointerException("the requiredType must not be null");
        }
        // 如果是基本数据类型或其包装类型，或 String 类型，则直接创建 handler
        if (Utils.isPrimitiveOrWrapper(requiredType)
            || String.class.equals(requiredType)) {
            return new BeanResultSetHandler<>(requiredType);
        }
        // 否则先从缓存中获取
        BeanResultSetHandler<R> handler = BEAN_HANDLER_CACHE.getValue(requiredType);
        // 如果不存在，创建 handler 并存入缓存
        if (handler == null) {
            handler = new BeanResultSetHandler<>(requiredType);
            BEAN_HANDLER_CACHE.putValue(requiredType, handler);
        }
        return handler;
    }

    private final Class<T> beanType;
    private final boolean isCommonClass;
    private Map<String, FieldInvoker> invokers;

    private BeanResultSetHandler(Class<T> type) {
        this.beanType = type;
        if (JdbcUtil.isJdbcCommonClass(type)) {
            isCommonClass = true;
        } else {
            isCommonClass = false;
            this.invokers = new HashMap<>();
            // 通过反射获取 Bean 信息
            Reflector reflector = ReflectionUtil.forEntityClass(type);
            boolean camelCaseToSnake = GlobalConfig.isCamelCaseToSnake();
            Iterator<FieldInvoker> iterator = reflector.fieldIterator();
            FieldInvoker fieldInvoker;
            // 将 Bean 中的属性解析到 Map 中，方便后面使用
            // eg: userName
            while (iterator.hasNext()) {
                fieldInvoker = iterator.next();
                String name = fieldInvoker.getName();
                this.invokers.put(name, fieldInvoker);// userName
                String upperName = name.toUpperCase();
                if (!name.equals(upperName)) {
                    this.invokers.put(upperName, fieldInvoker);// USERNAME
                }
                if (camelCaseToSnake) {
                    String lineName = Utils.camelCaseToSnake(name);
                    if (!invokers.containsKey(lineName)) {
                        this.invokers.put(lineName, fieldInvoker);// user_name
                    }
                    String lineUpperName = lineName.toUpperCase();// USER_NAME
                    if (!invokers.containsKey(lineUpperName)) {
                        this.invokers.put(lineUpperName, fieldInvoker);
                    }
                }
            }
        }
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        return handle(rs, true);
    }

    @SuppressWarnings("unchecked")
    T handle(ResultSet rs, boolean next) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int colCount = metaData.getColumnCount();
        if (colCount == 0) {
            return null;
        }
        boolean hasNext;
        if (next) {
            hasNext = rs.next();
        } else {
            hasNext = true;
        }
        if (hasNext) {
            // 如果是 JDBC 通用的数据类型，则直接获取该类型的数据
            if (isCommonClass) {
                return (T) JdbcUtil.getResultSetValue(rs, 1, beanType);
            } else {
                T result = Utils.instantiate(beanType);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = JdbcUtil.getColumnName(metaData, i);
                    FieldInvoker fieldInvoker = this.invokers.get(columnName);
                    if (fieldInvoker != null) {
                        Object value = JdbcUtil.getResultSetValue(rs, i, fieldInvoker.getType());
                        fieldInvoker.setValue(result, value);
                    }
                }
                return result;
            }
        }
        return null;
    }

}
