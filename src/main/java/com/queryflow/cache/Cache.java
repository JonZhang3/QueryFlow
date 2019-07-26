package com.queryflow.cache;

import java.util.Map;

public interface Cache<K, V> extends Iterable<Map.Entry<K, V>> {

    int size();

    V getValue(K key);

    void putValue(K key, V value);

    void putValues(Cache<K, V> cache);

    V removeValue(K key);

    void clearValues();

    boolean isEmpty();

    boolean isFull();

}
