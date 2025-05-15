package com.BallaDream.BallaDream.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseCustomException extends RuntimeException {
    private final int status;
    private final String errorMessage;

    protected BaseCustomException(int status, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

