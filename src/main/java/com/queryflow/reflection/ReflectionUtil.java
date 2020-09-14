package com.queryflow.reflection;

import com.queryflow.cache.Cache;
import com.queryflow.cache.impl.LFUCache;
import com.queryflow.reflection.entity.EntityReflector;

public class ReflectionUtil {

    private static final Cache<Class<?>, Reflector> CACHE = new LFUCache<>();

    private ReflectionUtil() {}

    public static Reflector forClass(Class clazz) {
        Reflector reflector = CACHE.getValue(clazz);
        if (reflector == null) {
            reflector = new Reflector(clazz);
            CACHE.putValue(clazz, reflector);
        }
        return reflector;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityReflector> T forEntityClass(Class<?> clazz) {
        Reflector reflector = CACHE.getValue(clazz);
        if (reflector == null) {
            reflector = new EntityReflector(clazz);
            CACHE.putValue(clazz, reflector);
        }
        return (T) reflector;
    }

}
