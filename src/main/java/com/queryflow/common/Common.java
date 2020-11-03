package com.queryflow.common;

import com.queryflow.utils.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Common {

    private static final Map<Class<? extends ColumnFillStrategy>, ColumnFillStrategy> FILL_STRATEGY
        = new ConcurrentHashMap<>();

    static {
        getFillStrategy(DefaultColumnFillStrategy.class);
    }

    public static ColumnFillStrategy getFillStrategy(Class<? extends ColumnFillStrategy> clazz) {
        return FILL_STRATEGY.computeIfAbsent(clazz, Utils::instantiate);
    }

}
