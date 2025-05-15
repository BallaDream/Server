package com.BallaDream.BallaDream.exception.diagnose;

import com.BallaDream.BallaDream.constants.ResponseCode;

public class DiagnoseNotFoundException extends DiagnoseException {
    public DiagnoseNotFoundException() {
        super(ResponseCode.DIAGNOSE_NOT_FOUND);
    }
}
