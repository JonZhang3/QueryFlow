package com.queryflow.cache;

import java.util.Iterator;
import java.util.Map;

public class CacheIterator<K, V> implements Iterator<Map.Entry<K, V>> {

    private final Iterator<Map.Entry<K, ? extends ValueObject<K, V>>> iterator;

    public CacheIterator(Iterator<Map.Entry<K, ? extends ValueObject<K, V>>> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Map.Entry<K, V> next() {
        Map.Entry<K, ? extends ValueObject<K, V>> next = iterator.next();
        return new CacheEntry<>(next.getKey(), next.getValue().get());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("this is a read-only iterator, cannot use remove opration.");
    }

    private class CacheEntry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private final V value;

        CacheEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("");
        }
    }

}
