package com.queryflow.log.log4j2;

import com.queryflow.log.Log;
import com.queryflow.log.LogFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Log4j2LoggerImpl implements Log {

    private static Marker MARKER = MarkerManager.getMarker(LogFactory.MARKER);

    private Logger log;

    public Log4j2LoggerImpl(Logger logger) {
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
        log.error(MARKER, s, e);
    }

    @Override
    public void error(String s) {
        log.error(MARKER, s);
    }

    @Override
    public void error(Throwable e) {
        log.error(MARKER, "", e);
    }

    @Override
    public void debug(String s) {
        if (isDebugEnabled()) {
            log.debug(MARKER, s);
        }
    }

    @Override
    public void debug(String s, Throwable e) {
        if (isDebugEnabled()) {
            log.debug(MARKER, s, e);
        }
    }

    @Override
    public void trace(String s) {
        log.trace(MARKER, s);
    }

    @Override
    public void warn(String s) {
        log.warn(MARKER, s);
    }

    @Override
    public void info(String msg) {
        log.info(MARKER, msg);
    }

}
