package com.BallaDream.BallaDream.constants;

public enum ResponseCode {
    //로그인할 때 토큰에 대한 예외 메시지
    WRONG_TOKEN("잘못된 토큰입니다.", 401),
    EXPIRED_TOKEN("만료된 토큰입니다.", 406),
    UNSUPPORTED_TOKEN("지원하지 않은 토큰입니다.", 415),
    ILLEGAL_TOKEN("부적절한 토큰입니다.", 400),
    //토큰을 재발급 받을때 예외 메시지
    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰이 존재하지 않습니다.", 400),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.", 400),
    EXPIRED_REFRESH_TOKEN("리프레시 토큰이 만료되었습니다.", 401),
    UNAUTHORIZED("인가받지 못한 상태입니다.", 401),
    LOGIN_FAIL("로그인에 실패하였습니다.", 401),
    ID_ALREADY_EXISTS("이미 존재하는 아이디 입니다.", 400),
    INVALID_AUTH_NUMBER("잘못된 인증번호 입니다.", 400),
    INVALID_LOGIN_PARAMETER("로그인 아이디나 패스워드를 입력하지 않았습니다.", 406),
    INVALID_USER("유효한 회원이 아닙니다.", 401),
    INVALID_INPUT_DATA("유효한 데이터가 아닙니다.", 401),
    INVALID_INPUT_FORMAT("유효하지 않은 입력값을 입력했습니다.", 400), //컨트롤러에서 입력 포맷이 잘못된 경우
    DIAGNOSE_ACCESS_ERROR("다른 사람의 진단 기록을 열람할 수 없습니다.", 403),
    DIAGNOSE_NOT_FOUND("진단 기록을 찾을 수 없습니다.", 404),
    ALREADY_INTERESTED_PRODUCT("이미 관심 제품으로 등록하였습니다.", 409),
    PRODUCT_NOT_FOUND("존재하지 않은 상품입니다.", 404);


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
