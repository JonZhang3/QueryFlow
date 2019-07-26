package com.queryflow.common;

public class QueryFlowException extends RuntimeException {

    public QueryFlowException() {
    }

    public QueryFlowException(String method, String message) {
        super(method + ": " + message);
    }

    public QueryFlowException(String message) {
        super(message);
    }

    public QueryFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryFlowException(Throwable cause) {
        super(cause);
    }

}
