package com.queryflow.utils;

/**
 * IO异常的非受检异常，当需要抛出 IOException 时，可用该类进行包裹
 *
 * @author Jon
 * @since 1.0.0
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException() {
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IORuntimeException(Throwable cause) {
        super(cause);
    }
}
