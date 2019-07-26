package com.queryflow.common.function;

@FunctionalInterface
public interface Action<T> {

    void apply(T t);

}
