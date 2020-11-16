package com.queryflow.reflection;

import com.queryflow.reflection.invoker.MethodInvoker;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 仅反射获取指定类的成员变量
 */
public class FieldReflector extends Reflector {

    public FieldReflector(Class<?> clazz) {
        this(clazz, true);
    }

    public FieldReflector(Class<?> clazz, boolean skipFinalField) {
        super(clazz, skipFinalField);
    }

    public FieldReflector(Class<?> clazz, boolean skipFinalField, boolean containParentFields) {
        super(clazz, containParentFields, skipFinalField);
    }

    @Override
    protected void initMethodInvokers() {
        this.methodInvokers = Collections.emptyMap();
    }

    @Override
    public Object invokeMethod(String methodName, Object target, Object... args) {
        throw new UnsupportedOperationException("this is a field reflector, cannot invoke method");
    }

    @Override
    public Iterator<MethodInvoker> methodIterator() {
        return new Iterator<MethodInvoker>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public MethodInvoker next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public MethodInvoker getMethodInvoker(String methodName) {
        return null;
    }
}
