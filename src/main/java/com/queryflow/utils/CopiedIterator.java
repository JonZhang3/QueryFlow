package com.queryflow.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 拷贝迭代器
 *
 * @author Jon
 * @since 1.0.0
 */
public class CopiedIterator<V> implements Iterator<V> {

    private List<V> replicas = new LinkedList<>();
    private Iterator<V> iterator;

    public CopiedIterator(Iterator<V> iterator) {
        while (iterator.hasNext()) {
            replicas.add(iterator.next());
        }
        this.iterator = replicas.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public V next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("this is a read-only iterator, cannot use remove opration.");
    }
}
