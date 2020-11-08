package com.queryflow.common;

import com.queryflow.common.type.TypeHandler;
import com.queryflow.utils.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Common 工具
 *
 * @since 1.2.0
 * @author Jon
 */
public final class Common {

    private static final Map<Class<? extends ColumnFillStrategy>, ColumnFillStrategy> FILL_STRATEGY
        = new ConcurrentHashMap<>();
    private static final Map<Class<? extends TypeHandler>, TypeHandler> TYPE_HANDLERS
        = new ConcurrentHashMap<>();

    static {
        getFillStrategy(DefaultColumnFillStrategy.class);
    }

    /**
     * 通过指定 {@code Class} 类型获取字段自动填充策略
     *
     * @param clazz 自定义的 {@code ColumnFillStrategy} 实现，提供自动填充字段值的策略
     * @return {@code ColumnFillStrategy} 实例
     * @see ColumnFillStrategy
     * @see DefaultColumnFillStrategy
     * @since 1.2.0
     */
    public static ColumnFillStrategy getFillStrategy(Class<? extends ColumnFillStrategy> clazz) {
        return FILL_STRATEGY.computeIfAbsent(clazz, Utils::instantiate);
    }

    /**
     * 通过指定 {@code Class} 类型获取字段类型处理器
     *
     * @param clazz 自定义的 {@code TypeHandler} 实现，提供类型转换
     * @return {@code TypeHandler} 实例
     * @see TypeHandler
     * @see com.queryflow.common.type.DefaultTypeHandler
     * @since 1.2.0
     */
    public static TypeHandler getTypeHandler(Class<? extends TypeHandler> clazz) {
        return TYPE_HANDLERS.computeIfAbsent(clazz, Utils::instantiate);
    }

}
