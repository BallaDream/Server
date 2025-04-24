package com.BallaDream.BallaDream.exception.data;

//어플리케이션을 초기에 띄울때 화장품 관련 데이터를 데이터베이스 삽입할 때 무결성 제약 조건을 해치는 경우 발생하는 예외
public class DataIntegrityException extends RuntimeException{

    public DataIntegrityException() {
    }

    public DataIntegrityException(String message) {
        super(message);
    }

    public DataIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIntegrityException(Throwable cause) {
        super(cause);
    }

    public DataIntegrityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
