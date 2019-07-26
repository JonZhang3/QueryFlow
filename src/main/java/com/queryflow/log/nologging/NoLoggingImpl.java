package com.queryflow.log.nologging;

import com.queryflow.log.Log;

public class NoLoggingImpl implements Log {

    public NoLoggingImpl(String clazz) {
        // Do Nothing
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void error(String s, Throwable e) {
        // Do Nothing
    }

    @Override
    public void error(String s) {
        // Do Nothing
    }

    @Override
    public void error(Throwable e) {

    }

    @Override
    public void debug(String s) {
        // Do Nothing
    }

    @Override
    public void debug(String s, Throwable e) {

    }

    @Override
    public void trace(String s) {
        // Do Nothing
    }

    @Override
    public void warn(String s) {
        // Do Nothing
    }

    @Override
    public void info(String msg) {
        // Do Nothing
    }

}
