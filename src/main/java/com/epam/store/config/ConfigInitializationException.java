package com.epam.store.config;

public class ConfigInitializationException extends RuntimeException {

    public ConfigInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigInitializationException(Throwable cause) {
        super(cause);
    }
}
