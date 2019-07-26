package com.queryflow.reflection.invoker;

public interface MethodInvoker extends Invoker {

    Object invoke(Object target, Object... args);

}
