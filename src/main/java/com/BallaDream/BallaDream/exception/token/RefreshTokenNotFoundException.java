package com.BallaDream.BallaDream.exception.token;

import com.BallaDream.BallaDream.constants.ResponseCode;

// refresh 토큰이 존재하지 않을때 예외 발생
public class RefreshTokenNotFoundException extends TokenException{
    public RefreshTokenNotFoundException() {
        super(ResponseCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
