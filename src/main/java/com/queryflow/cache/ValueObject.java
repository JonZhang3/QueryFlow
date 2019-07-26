package com.queryflow.cache;

public class ValueObject<K, V> {

    protected final K key;
    protected final V value;

    public ValueObject(K k, V v) {
        this.key = k;
        this.value = v;
    }

    public boolean isExpired() {
        return false;
    }

    public V get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
