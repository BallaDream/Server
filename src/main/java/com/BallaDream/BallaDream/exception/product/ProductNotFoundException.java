package com.BallaDream.BallaDream.exception.product;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.exception.user.UserException;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException() {
        super(ResponseCode.PRODUCT_NOT_FOUND);
    }
}
