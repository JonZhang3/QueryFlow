package com.queryflow.log;

public interface Log {

    boolean isDebugEnabled();

    boolean isTraceEnabled();

    void error(String msg, Throwable e);

    void error(String msg);

    void error(Throwable e);

    void debug(String s);

    void debug(String s, Throwable e);

    void trace(String s);

    void warn(String msg);

    void info(String msg);

}
