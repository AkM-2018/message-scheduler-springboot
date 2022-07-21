package com.akm.messagescheduler.error;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(java.lang.String message) {
        super(message);
    }

    public InvalidTokenException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(java.lang.Throwable cause) {
        super(cause);
    }

    protected InvalidTokenException(java.lang.String message, java.lang.Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
