package com.BallaDream.BallaDream.common;

import jakarta.servlet.http.Cookie;

import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;

public class CookieUtil {

    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static Cookie deleteRefreshTokenInCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN.getType(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
