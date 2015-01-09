package com.epam.store.action;

public class ActionException extends RuntimeException {
    public ActionException() {
        super();
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(Throwable cause) {
        super(cause);
    }
}
