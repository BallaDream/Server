package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class UserException extends BaseCustomException {

    public UserException(ResponseCode responseCode) {
        super(responseCode.getCode(), responseCode.getMessage());
    }
}
