package com.queryflow.accessor;

public interface AccessorFactory {

    String DEFAULT_TAG = "default";

    Accessor getAccessor();

    Accessor getAccessor(String tag);

    Accessor getAccessor(int index);

    int size();

    boolean containTag(String tag);

    static Accessor accessor() {
        return AccessorManager.accessor();
    }

    static Accessor accessor(int index) {
        return AccessorManager.accessor(index);
    }

    static Accessor accessor(String tag) {
        return AccessorManager.accessor(tag);
    }

}
