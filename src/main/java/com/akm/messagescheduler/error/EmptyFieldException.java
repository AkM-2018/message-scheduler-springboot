package com.akm.messagescheduler.error;

public class EmptyFieldException extends Exception {

    public EmptyFieldException() {
        super();
    }

    public EmptyFieldException(java.lang.String message) {
        super(message);
    }

    public EmptyFieldException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public EmptyFieldException(java.lang.Throwable cause) {
        super(cause);
    }

    protected EmptyFieldException(java.lang.String message, java.lang.Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
