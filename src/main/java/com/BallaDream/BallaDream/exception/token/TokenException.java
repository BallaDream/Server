package com.BallaDream.BallaDream.exception.token;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class TokenException extends BaseCustomException {

    public TokenException(ResponseCode responseCode) {
        super(responseCode.getCode(), responseCode.getMessage());
    }
}
