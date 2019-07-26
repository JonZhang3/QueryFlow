package com.queryflow.log.slf4j;

import com.queryflow.log.Log;
import org.slf4j.Logger;

class Slf4jLoggerImpl implements Log {

    private Logger log;

    public Slf4jLoggerImpl(Logger logger) {
        log = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(s, e);
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void error(Throwable e) {
        log.error("", e);
    }

    @Override
    public void debug(String s) {
        if (isDebugEnabled()) {
            log.debug(s);
        }
    }

    @Override
    public void debug(String s, Throwable e) {
        if (isDebugEnabled()) {
            log.debug(s, e);
        }
    }

    @Override
    public void trace(String s) {
        log.trace(s);
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

}
