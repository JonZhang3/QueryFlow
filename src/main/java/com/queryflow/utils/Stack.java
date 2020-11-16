package com.queryflow.utils;

import java.util.LinkedList;

/**
 * 栈的简单实现，内部使用 {@code LinkedList}，用于组装 SQL 语句
 *
 * @param <T>
 * @since 1.2.0
 * @author Jon
 */
public class Stack<T> {

    private final LinkedList<T> container = new LinkedList<>();

    public T peek() {
        return container.peek();
    }

    public Stack<T> push(T t) {
        container.push(t);
        return this;
    }

    public T pop() {
        return container.pop();
    }

    public int size() {
        return container.size();
    }

    public StringBuilder toStr() {
        StringBuilder builder = new StringBuilder();
        int size = container.size();
        for (int i = size - 1; i >= 0; i--) {
            builder.append(container.get(i));
        }
        return builder;
    }

}
