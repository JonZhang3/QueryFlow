package com.queryflow.key;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class KeyGenerateUtil {

    private KeyGenerateUtil() {
    }

    private static final Map<Class<? extends KeyGenerator>, KeyGenerator> generators =
        new HashMap<>(2);

    static {
        registerKeyGenerator(SnowflakeKeyGenerator.class);
        registerKeyGenerator(AutoIncrementKeyGenerator.class);
        registerKeyGenerator(UUIDKeyGenerator.class);
        registerKeyGenerator(SimpleKeyGenerator.class);
    }

    public static void registerKeyGenerator(Class<? extends KeyGenerator> clazz) {
        Assert.notNull(clazz);

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("the key generator must not be interface or abstract class");
        }
        KeyGenerator keyGenerator = Utils.instantiate(clazz);
        generators.put(clazz, keyGenerator);
    }

    public static KeyGenerator getKeyGenerator(Class<? extends KeyGenerator> clazz) {
        return generators.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T generateId(Class<? extends KeyGenerator<T>> clazz) {
        KeyGenerator keyGenerator = generators.get(clazz);
        if (keyGenerator == null) {
            throw new QueryFlowException("cannot found your key generator " + clazz.getName() + ", you must register it");
        }
        return (T) keyGenerator.generate();
    }

    /**
     * 生成 ID，默认为使用 snowflake 算法
     *
     * @see SnowflakeKeyGenerator
     * @return 生成的 ID
     */
    public static Long generateId() {
        return (Long) generators.get(SnowflakeKeyGenerator.class).generate();
    }

    public static int keyGeneratorSize() {
        return generators.size();
    }

    public static boolean containKeyGenerator(Class<? extends KeyGenerator> clazz) {
        return generators.containsKey(clazz);
    }

}
