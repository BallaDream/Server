package com.BallaDream.BallaDream.common;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;

public class CookieUtil {

    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static ResponseCookie createRefreshCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
    }

    public static Cookie deleteRefreshTokenInCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN.getType(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
