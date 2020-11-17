package com.queryflow.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class LinkedMultiValueMap<K, V, O> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, List<Object>> container;
    private final int valueSize;

    public LinkedMultiValueMap(int valueSize) {
        container = new LinkedHashMap<>();
        this.valueSize = valueSize;
    }

    public O getObject(String key) {
        List<Object> objects = container.get(key);
        return objects == null ? null : (O) objects.get(valueSize - 1);
    }



}
