package com.queryflow.reflection.invoker;

import com.queryflow.reflection.ReflectionException;
import com.queryflow.utils.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class NormalMethod implements MethodInvoker {

    private final Method method;

    public NormalMethod(Method method) {
        Assert.notNull(method);

        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    public Object invoke(Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    @Override
    public String getName() {
        return method.getName();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public boolean isHide() {
        return !Modifier.isPublic(method.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(method.getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isStatic()) {
            sb.append("static ");
        }
        if (isAbstract()) {
            sb.append("abstract ");
        }
        sb.append(method.getReturnType().getName())
            .append(" ")
            .append(method.getName()).append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0, len = parameterTypes.length; i < len; i++) {
            sb.append(parameterTypes[i].getName());
            if (i < (len - 1)) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
