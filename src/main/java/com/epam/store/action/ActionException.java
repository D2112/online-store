package com.epam.store.action;

public class ActionException extends RuntimeException {
    public ActionException() {
        super();
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionException(Throwable cause) {
        super(cause);
    }
}
