package com.queryflow.cache.impl;

import com.queryflow.cache.Cache;
import com.queryflow.cache.CacheIterator;
import com.queryflow.cache.ValueObject;
import com.queryflow.utils.Assert;
import com.queryflow.utils.CopiedIterator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> extends LinkedHashMap<K, ValueObject<K, V>> implements Cache<K, V> {

    private static final int DEFAULT_MAX_SIZE = 300;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private final int maxSize;

    public LRUCache() {
        this(DEFAULT_MAX_SIZE);
    }

    public LRUCache(int maxSize) {
        super(16, 0.75f, false);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, ValueObject<K, V>> eldest) {
        return this.size() > maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public int size() {
        try {
            readLock.lock();
            return super.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V getValue(K key) {
        ValueObject<K, V> valueObject = this.get(key);
        return valueObject == null ? null : valueObject.get();
    }

    @Override
    public ValueObject<K, V> get(Object key) {
        Assert.notNull(key, "the key must not be null");
        try {
            readLock.lock();
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isFull() {
        try {
            readLock.lock();
            return this.size() >= maxSize;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            readLock.lock();
            return super.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void putValue(K key, V value) {
        Assert.notNull(value);
        this.put(key, new ValueObject<>(key, value));
    }

    @Override
    public ValueObject<K, V> put(K key, ValueObject<K, V> value) {
        Assert.notNull(key);
        Assert.notNull(value);

        try {
            writeLock.lock();
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void putValues(Cache<K, V> cache) {
        if (cache == null) {
            return;
        }
        try {
            writeLock.lock();
            Iterator<Map.Entry<K, V>> iterator = cache.iterator();
            Map.Entry<K, V> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                K key = entry.getKey();
                super.put(key, new ValueObject<>(key, entry.getValue()));
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends ValueObject<K, V>> m) {
        if (m == null) {
            return;
        }
        try {
            writeLock.lock();
            super.putAll(m);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V removeValue(K key) {
        ValueObject<K, V> removedValue = this.remove(key);
        return removedValue == null ? null : removedValue.get();
    }

    @Override
    public ValueObject<K, V> remove(Object key) {
        if (key == null) {
            return null;
        }
        try {
            writeLock.lock();
            return super.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clearValues() {
        this.clear();
    }

    @Override
    public void clear() {
        try {
            writeLock.lock();
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Map.Entry<K, V>> iterator() {
        try {
            readLock.lock();
            Iterator<Map.Entry<K, ValueObject<K, V>>> iterator = this.entrySet().iterator();
            CopiedIterator copiedIterator = new CopiedIterator(iterator);
            return new CacheIterator<>(copiedIterator);
        } finally {
            readLock.unlock();
        }
    }

}
