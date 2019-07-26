package com.queryflow.reflection;

import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.reflection.invoker.MethodInvoker;
import com.queryflow.reflection.invoker.NormalMethod;
import com.queryflow.reflection.invoker.Property;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 反射指定类信息
 */
public class Reflector {

    protected Class<?> type;
    protected Constructor defaultConstructor;
    protected Map<String, FieldInvoker> fieldInvokers;
    protected Map<String, MethodInvoker> methodInvokers;
    protected boolean skipFinalField;

    public Reflector(Class<?> clazz) {
        this(clazz, true);
    }

    public Reflector(Class<?> clazz, boolean skipFinalField) {
        Assert.notNull(clazz);

        this.type = clazz;
        defaultConstructor();
        initFieldInvokers();
        initMethodInvokers();
        this.skipFinalField = skipFinalField;
    }

    // 初始化默认构造方法
    private void defaultConstructor() {
        defaultConstructor = Utils.getDefaultConstructor(type);
    }

    // 初始化成员变量（包括静态、常量）
    protected void initFieldInvokers() {
        Field[] fields = type.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            fieldInvokers = new HashMap<>(fields.length);
            for (Field field : fields) {
                if (!skipFinalField && !Modifier.isFinal(field.getModifiers())) {
                    fieldInvokers.put(field.getName(), createFieldInvoker(field));
                }
            }
        } else {
            fieldInvokers = Collections.emptyMap();
        }
    }

    // 初始化类方法
    protected void initMethodInvokers() {
        Method[] methods = type.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            methodInvokers = new HashMap<>(methods.length);
            for (Method method : methods) {
                methodInvokers.put(method.getName(), createMethodInvoker(method));
            }
        } else {
            methodInvokers = Collections.emptyMap();
        }
    }

    protected FieldInvoker createFieldInvoker(Field field) {
        return new Property(field);
    }

    protected MethodInvoker createMethodInvoker(Method method) {
        return new NormalMethod(method);
    }

    /**
     * 给目标对象中指定字段赋值
     * @param fieldName 字段名称
     * @param target 目标类
     * @param value 所要赋的值
     */
    public void setFieldValue(String fieldName, Object target, Object value) {
        FieldInvoker fieldInvoker = fieldInvokers.get(fieldName);
        if (fieldInvoker == null) {
            throw new ReflectionException("no such field " + fieldName + " in " + type.getName());
        }
        fieldInvoker.setValue(target, value);
    }

    /**
     * 获取目标对象中指定字段的值
     * @param fieldName 字段名称
     * @param target 类的实例对象
     * @return the field value
     */
    public Object getFieldValue(String fieldName, Object target) {
        FieldInvoker fieldInvoker = fieldInvokers.get(fieldName);
        if (fieldInvoker == null) {
            throw new ReflectionException("no such field " + fieldName + " in "
                + type.getName() + ", or the field is final");
        }
        return fieldInvoker.getValue(target);
    }

    /**
     * 执行目标对象中指定的方法
     * @param methodName 方法名
     * @param target 目标对象
     * @param args 方法中的参数
     * @return 指定方法执行后的返回值
     */
    public Object invokeMethod(String methodName, Object target, Object... args) {
        MethodInvoker methodInvoker = methodInvokers.get(methodName);
        if (methodInvoker == null) {
            throw new ReflectionException("no such method " + methodName + " in " + type.getName());
        }
        return methodInvoker.invoke(target, args);
    }

    /**
     * 使用默认的构造方法初始化该类的实例
     * @return 实例化的对象
     */
    public Object newInstance() {
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * 类型
     * @return Class
     */
    public Class<?> getType() {
        return type;
    }

    public FieldInvoker getFieldInvoker(String fieldName) {
        return fieldInvokers.get(fieldName);
    }

    public MethodInvoker getMethodInvoker(String methodName) {
        return methodInvokers.get(methodName);
    }

    /**
     * 遍历类的成员变量
     * @return {@link FieldInvoker}
     */
    public Iterator<FieldInvoker> fieldIterator() {
        return fieldInvokers.values().iterator();
    }

    /**
     * 遍历类方法
     * @return {@link MethodInvoker}
     */
    public Iterator<MethodInvoker> methodIterator() {
        return methodInvokers.values().iterator();
    }

    public int fieldSize() {
        return fieldInvokers.size();
    }

    /**
     * 返回类中包含方法的数量
     * @return 返回类中方法的数量
     */
    public int methodSize() {
        return methodInvokers.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(type.getName());
        sb.append("{fields: ")
            .append(fieldInvokers.values())
            .append("; methods: ")
            .append(methodInvokers.values())
            .append("}");
        return sb.toString();
    }
}
