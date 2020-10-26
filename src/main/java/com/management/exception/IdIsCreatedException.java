package com.management.exception;

import org.springframework.security.core.AuthenticationException;

public class IdIsCreatedException extends Exception {
    public IdIsCreatedException() {
        super();
    }

    public IdIsCreatedException(String message) {
        super(message);
    }

    public IdIsCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdIsCreatedException(Throwable cause) {
        super(cause);
    }

    protected IdIsCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
