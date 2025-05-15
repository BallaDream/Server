package com.BallaDream.BallaDream.exception.token;

import com.BallaDream.BallaDream.constants.ResponseCode;

// refresh 토큰이 redis 에 없는데 재발급을 신청했을 때, 해당 토큰이 유효하지 않다고 판단하여 아래의 예외를 던진다.
public class InvalidRefreshTokenException extends TokenException{
    public InvalidRefreshTokenException() {
        super(ResponseCode.INVALID_REFRESH_TOKEN);
    }
}
