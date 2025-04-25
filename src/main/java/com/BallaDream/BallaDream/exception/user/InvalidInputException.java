package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;

public class InvalidInputException extends UserException {
    public InvalidInputException() {
        super(ResponseCode.INVALID_INPUT_DATA);
    }
}
