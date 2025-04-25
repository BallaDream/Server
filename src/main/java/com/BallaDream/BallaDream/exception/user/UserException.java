package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;
import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{

    private final ResponseCode responseCode;

    public UserException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public HttpStatus getStatus() {
        return HttpStatus.valueOf(responseCode.getCode());
    }

    public String getErrorMessage() {
        return responseCode.getMessage();
    }
}
