package com.queryflow.mapper;

import com.queryflow.common.QueryFlowException;
import com.queryflow.annotation.Mapper;
import com.queryflow.mapper.proxy.CglibProxyFactory;
import com.queryflow.mapper.proxy.MapperMethodInterceptor;
import com.queryflow.mapper.proxy.ProxyFactory;
import com.queryflow.utils.Assert;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper 帮助类
 *
 * @author Jon
 * @since 1.0.0
 */
public final class MapperManager {

    // Mapper 容器
    private static final Map<Class<?>, Object> MAPPER_CLASS = new HashMap<>();

    // cglib 代理
    private static final ProxyFactory PROXY_FACTORY = new CglibProxyFactory();

    private MapperManager() {
    }

    /**
     * 新增 Mapper 类，可指定是否将其注册到 Spring 容器中。
     *
     * @param clazz            Mapper Class
     * @param registerToSpring 是否注册到 Spring 容器中
     * @param beanFactory      Spring 容器
     * @return 代理后的 Mapper 实例
     */
    public static Object addMapperClass(Class<?> clazz, boolean registerToSpring, BeanFactory beanFactory) {
        Assert.notNull(clazz);
        if (clazz.getAnnotation(Mapper.class) == null) {
            throw new QueryFlowException(clazz.getName() + " not a mapper class");
        }
        Object proxy = MAPPER_CLASS.get(clazz);
        if (proxy == null) {
            synchronized (MAPPER_CLASS) {
                proxy = MAPPER_CLASS.get(clazz);
                if (proxy == null) {
                    proxy = PROXY_FACTORY.createProxy(clazz, new MapperMethodInterceptor());
                    MAPPER_CLASS.put(clazz, proxy);
                }
            }
        }
        if (registerToSpring && beanFactory != null) {
            final ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
            configurableBeanFactory.registerSingleton(clazz.getName(), proxy);
        }
        return proxy;
    }

    public static Object addMapperClass(Class<?> clazz) {
        return addMapperClass(clazz, false, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getMapperClass(Class<T> clazz) {
        Assert.notNull(clazz);
        Object obj = MAPPER_CLASS.get(clazz);
        if (obj == null) {
            throw new QueryFlowException(clazz.getName() + " not a mapper class");
        }
        return (T) obj;
    }

}
