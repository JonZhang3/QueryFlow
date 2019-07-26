package com.queryflow.utils;

/**
 *
 * @author Jon
 * @since 1.0.0
 */
@FunctionalInterface
public interface Filter<T> {

    boolean accept(T clazz);

}
