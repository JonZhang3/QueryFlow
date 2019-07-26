package com.queryflow.log.commons;

import com.queryflow.log.Log;
import org.apache.commons.logging.LogFactory;

public class JakartaCommonsLoggingImpl implements Log {

    private org.apache.commons.logging.Log log;

    public JakartaCommonsLoggingImpl(String clazz) {
        log = LogFactory.getLog(clazz);
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
