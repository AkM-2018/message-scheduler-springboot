package com.akm.messagescheduler.error;

public class InvalidDateException extends Exception {

    public InvalidDateException() {
        super();
    }

    public InvalidDateException(java.lang.String message) {
        super(message);
    }

    public InvalidDateException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(java.lang.Throwable cause) {
        super(cause);
    }

    protected InvalidDateException(java.lang.String message, java.lang.Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
