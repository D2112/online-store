package com.epam.store.metadata;

public class MetadataException extends RuntimeException {

    public MetadataException(Throwable cause) {
        super(cause);
    }

    public MetadataException(String message) {
        super(message);
    }

    public MetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
