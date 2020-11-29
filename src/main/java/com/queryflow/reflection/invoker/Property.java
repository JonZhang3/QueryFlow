package com.queryflow.reflection.invoker;

import com.queryflow.common.QueryFlowException;
import com.queryflow.reflection.ReflectionException;
import com.queryflow.utils.Assert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Property implements FieldInvoker {

    protected Field field;
    private MethodInvoker setter;
    private MethodInvoker getter;

    public Property(Field field) {
        Assert.notNull(field);

        init(field);
    }

    public Property(String propertyName, Class<?> clazz) {
        Assert.notNull(propertyName);
        Assert.notNull(clazz);

        Field field = getField(propertyName, clazz);
        if (field == null) {
            throw new ReflectionException("no such field " + propertyName + " in " + clazz.getName());
        }
        init(field);
    }

    private void init(Field field) {
        this.field = field;
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
            Method setterMethod = descriptor.getWriteMethod();
            if (setterMethod != null) {
                setter = new NormalMethod(setterMethod);
            }
            Method getterMethod = descriptor.getReadMethod();
            if (getterMethod != null) {
                getter = new NormalMethod(getterMethod);
            }
            if (setterMethod == null || getterMethod == null) {
                try {
                    this.field.setAccessible(true);
                } catch (Exception ignore) {
                }
            }
        } catch (IntrospectionException e) {
            throw new QueryFlowException(e);
        }
    }

    @Override
    public Object getValue(Object target) {
        if (getter != null) {
            return getter.invoke(target);
        } else {
            try {
                return field.get(target);
            } catch (IllegalAccessException e) {
                throw new ReflectionException(e);
            }
        }
    }

    @Override
    public void setValue(Object target, Object value) {
        if (setter != null) {
            setter.invoke(target, value);
        } else {
            try {
                this.field.set(target, value);
            } catch (IllegalAccessException e) {
                throw new ReflectionException(e);
            }
        }
    }

    public Annotation getAnnotation(Class<? extends Annotation> clazz) {
        return field.getAnnotation(clazz);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isHide() {
        return !Modifier.isPublic(field.getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    @Override
    public boolean readable() {
        return field.isAccessible() || getter != null;
    }

    @Override
    public boolean writeable() {
        return (!isFinal()) && (field.isAccessible() || setter != null);
    }

    private Field getField(String fieldName, Class<?> clazz) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(field.getName());
        sb.append("{");
        if (isStatic()) {
            sb.append("static ");
        }
        if (isFinal()) {
            sb.append("final ");
        }
        sb.append("type=")
            .append(field.getType().getName())
            .append(", ").append("setter=")
            .append(setter).append(", getter=")
            .append(getter).append("}");
        return sb.toString();
    }

}
