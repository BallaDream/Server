package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;

public class AlreadyWebUserException extends UserException {
    public AlreadyWebUserException() {
        super(ResponseCode.ALREADY_WEB_USER);
    }
}
