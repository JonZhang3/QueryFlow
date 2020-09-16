package com.queryflow.mapper.proxy;

import com.queryflow.common.QueryFlowException;
import com.queryflow.mapper.MapperMethod;
import com.queryflow.mapper.MapperMethodBuilder;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MapperMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!Modifier.isAbstract(method.getModifiers())) {
            return proxy.invokeSuper(obj, args);
        } else {
            MapperMethod mapperMethod = MapperMethodBuilder.getMapperMethod(method);
            if (mapperMethod == null) {
                throw new QueryFlowException("the method is not a mapper method: " + method.getName());
            }
            return mapperMethod.execute(obj, args);
        }
    }

}
