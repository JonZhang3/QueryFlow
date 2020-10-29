package com.queryflow.utils;

public final class Assert {

    private Assert() {}

    public static void notNull(Object obj) {
        notNull(obj, "this argument is required, it must not be null");
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str) {
        if (Utils.isEmpty(str)) {
            throw new IllegalArgumentException("this string argument must not be null or empty");
        }
    }

    public static void hasText(String str) {
        hasText(str, "this string argument must not be null, empty, or blank");
    }

    public static void hasText(String str, String message) {
        if (Utils.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notThis(Object src, Object dest) {
        if(src == dest) {
            throw new IllegalArgumentException("");
        }
    }

}
