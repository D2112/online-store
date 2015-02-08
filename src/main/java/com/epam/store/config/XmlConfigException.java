package com.epam.store.config;

public class XmlConfigException extends RuntimeException {

    public XmlConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlConfigException(Throwable cause) {
        super(cause);
    }
}
