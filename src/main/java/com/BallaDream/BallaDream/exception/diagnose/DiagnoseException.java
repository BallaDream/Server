package com.BallaDream.BallaDream.exception.diagnose;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

//진단 관련 예외
public class DiagnoseException extends BaseCustomException {

    public DiagnoseException(ResponseCode responseCode) {
        super(responseCode.getCode(), responseCode.getMessage());
    }
}
