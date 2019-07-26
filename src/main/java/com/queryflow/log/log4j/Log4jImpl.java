package com.queryflow.log.log4j;

import com.queryflow.log.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jImpl implements Log {

    private static final String FQCN = Log4jImpl.class.getName();

    private Logger log;

    public Log4jImpl(String clazz) {
        log = Logger.getLogger(clazz);
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
        log.log(FQCN, Level.ERROR, s, e);
    }

    @Override
    public void error(String s) {
        log.log(FQCN, Level.ERROR, s, null);
    }

    @Override
    public void error(Throwable e) {
        log.log(FQCN, Level.ERROR, "", e);
    }

    @Override
    public void debug(String s) {
        if (isDebugEnabled()) {
            log.log(FQCN, Level.DEBUG, s, null);
        }
    }

    @Override
    public void debug(String s, Throwable e) {
        if (isDebugEnabled()) {
            log.log(FQCN, Level.DEBUG, s, e);
        }
    }

    @Override
    public void trace(String s) {
        log.log(FQCN, Level.TRACE, s, null);
    }

    @Override
    public void warn(String s) {
        log.log(FQCN, Level.WARN, s, null);
    }

    @Override
    public void info(String msg) {
        log.log(FQCN, Level.INFO, msg, null);
    }

}
