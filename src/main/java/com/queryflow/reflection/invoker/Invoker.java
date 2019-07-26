package com.queryflow.reflection.invoker;

public interface Invoker {

    boolean isStatic();

    boolean isFinal();

    boolean isHide();

    String getName();

}
