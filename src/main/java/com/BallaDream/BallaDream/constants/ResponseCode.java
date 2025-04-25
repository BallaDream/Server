package com.BallaDream.BallaDream.constants;

public enum ResponseCode {
    WRONG_TOKEN("잘못된 토큰입니다.", 401),
    EXPIRED_TOKEN("만료된 토큰입니다.", 406),
    UNSUPPORTED_TOKEN("지원하지 않은 토큰입니다.", 415),
    ILLEGAL_TOKEN("부적절한 토큰입니다.", 400),
    UNAUTHORIZED("인가받지 못한 상태입니다.", 401),
    LOGIN_FAIL("로그인에 실패하였습니다.", 401),
    ID_ALREADY_EXISTS("이미 존재하는 아이디 입니다.", 400),
    INVALID_AUTH_NUMBER("잘못된 인증번호 입니다.", 400),
    INVALID_LOGIN_PARAMETER("로그인 아이디나 패스워드를 입력하지 않았습니다.", 406),
    INVALID_USER("유효한 회원이 아닙니다.", 401),
    INVALID_INPUT_FORMAT("유효하지 않은 입력값을 입력했습니다.", 400);


    private final String message;
    private final int code;


    ResponseCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
