package com.BallaDream.BallaDream.exception.user;

import com.BallaDream.BallaDream.constants.ResponseCode;

//이메일 인증 번호를 잘못 입력한 예외
public class InvalidMailAuthNumException extends UserException {

    public InvalidMailAuthNumException() {
        super(ResponseCode.INVALID_AUTH_NUMBER);
    }
}
