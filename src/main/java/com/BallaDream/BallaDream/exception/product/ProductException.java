package com.BallaDream.BallaDream.exception.product;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.BaseCustomException;

public class ProductException extends BaseCustomException {
    public ProductException(ResponseCode responseCode) {
        super(responseCode.getCode(), responseCode.getMessage());
    }
}
