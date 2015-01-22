package com.epam.store.metadata;

public class MetadataException extends RuntimeException {
    public MetadataException() {
        super();
    }

    public MetadataException(String message) {
        super(message);
    }

    public MetadataException(Throwable cause) {
        super(cause);
    }
}
