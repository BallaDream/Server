package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;

public class AlreadyInterestedProductException extends UserException{
    public AlreadyInterestedProductException() {
        super(ResponseCode.ALREADY_INTERESTED_PRODUCT);
    }
}
