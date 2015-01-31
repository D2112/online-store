package com.epam.store.listener;

public class ApplicationInitializationException extends RuntimeException {

    public ApplicationInitializationException() {
        super();
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }
}
