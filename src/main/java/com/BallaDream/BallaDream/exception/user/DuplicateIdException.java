package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;

public class DuplicateIdException extends UserException{
    public DuplicateIdException() {
        super(ResponseCode.ID_ALREADY_EXISTS);
    }
}
