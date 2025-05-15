package com.BallaDream.BallaDream.exception.diagnose;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.user.UserException;

//진단 기록의 소유자가 아닌 경우 발생하는 예외 ex) A 사용자가 B 사용자의 진단 기록을 열람하려고 할때
public class DiagnoseOwnershipException extends DiagnoseException {

    public DiagnoseOwnershipException() {
        super(ResponseCode.DIAGNOSE_ACCESS_ERROR);
    }
}
