package com.queryflow.common;

/**
 * Dictionary enum of interface.
 */
public interface DictionaryEnum<T> {

    /**
     * 代码值
     * @return 代码值
     */
    T getCode();

    /**
     * 代码值示意
     * @return String
     */
    String getValue();

}
