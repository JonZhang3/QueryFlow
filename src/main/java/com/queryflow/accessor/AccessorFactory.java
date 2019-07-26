package com.queryflow.accessor;

public interface AccessorFactory {

    String DEFAULT_TAG = "queryflow";

    Accessor getAccessor();

    Accessor getAccessor(String tag);

    Accessor getAccessor(int index);

    int size();

    boolean containTag(String tag);

    static Accessor accessor() {
        return AccessorManager.manager().getAccessor();
    }

    static Accessor accessor(int index) {
        return AccessorManager.manager().getAccessor(index);
    }

    static Accessor accessor(String tag) {
        return AccessorManager.manager().getAccessor(tag);
    }

}
