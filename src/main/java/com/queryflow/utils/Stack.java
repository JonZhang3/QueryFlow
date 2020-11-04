package com.queryflow.utils;

import java.util.LinkedList;

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
