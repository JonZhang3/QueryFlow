package com.queryflow.utils;

public class ClassInstantiationException extends RuntimeException {

    public ClassInstantiationException(Class<?> clazz, String message) {
        this(clazz, message, null);
    }

    public ClassInstantiationException(Class<?> clazz, String message, Throwable cause) {
        super("Failed to instantiate [" + clazz.getName() + "]: " + message, cause);
    }

}
