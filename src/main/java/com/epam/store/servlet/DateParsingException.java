package com.epam.store.servlet;

public class DateParsingException extends RuntimeException {

    public DateParsingException(String message) {
        super(message);
    }

    public DateParsingException(Throwable cause) {
        super(cause);
    }
}
