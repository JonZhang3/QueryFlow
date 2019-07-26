package com.queryflow.mapper.proxy;

import net.sf.cglib.proxy.Callback;

public interface ProxyFactory {

    <T> T createProxy(Class<T> clazz, Callback callback);

}
