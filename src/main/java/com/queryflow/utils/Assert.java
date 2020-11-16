package com.queryflow.utils;

import java.util.Map;

public final class Assert {

    private Assert() {}

    public static void notNull(Object obj) {
        notNull(obj, "this parameter is required, it must not be null");
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str) {
        if (Utils.isEmpty(str)) {
            throw new IllegalArgumentException("this string parameter must not be null or empty");
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        if(map == null || map.size() == 0) {
            throw new IllegalArgumentException("this parameter is required, it must not be null or empty");
        }
    }

    public static void hasText(String str) {
        hasText(str, "this string parameter must not be null, empty, or blank");
    }

    public static void hasText(String str, String message) {
        if (Utils.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断指定对象 {@code src} 与当前对象 {@code this} 不相等
     *
     * @param src 指定的对象
     * @param thisObj 当前对象，即 {@code this}
     * @throws IllegalArgumentException 如果两个对象相等，则抛出该异常
     * @since 1.2.0
     */
    public static void notThis(Object src, Object thisObj) {
        if(src == thisObj) {
            throw new IllegalArgumentException("");
        }
    }

}
