package com.BallaDream.BallaDream.exception.token;

import com.BallaDream.BallaDream.constants.ResponseCode;

//refresh 토큰이 만료되었을 때 발생하는 예외
public class ExpiredRefreshTokenException extends TokenException{
    public ExpiredRefreshTokenException() {
        super(ResponseCode.EXPIRED_REFRESH_TOKEN);
    }
}
