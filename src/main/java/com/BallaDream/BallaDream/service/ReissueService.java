package com.BallaDream.BallaDream.service;

import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final RedisUtil redisUtil;
    private final JWTUtil jwtUtil;

    //redis에 refresh 토큰이 저장됐는지 체크
    public Boolean isExist(String refreshToken) {
        return StringUtils.hasText(redisUtil.getData(refreshToken));
    }

    public String getTokenInCookie(HttpServletRequest request) {
        String refresh = null;
        //쿠키에서 refresh 토큰 꺼내기
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN.getType())) {
                refresh = cookie.getValue();
            }
        }

        return refresh;
    }

    //logout 시 refresh 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {
        redisUtil.deleteData(refreshToken);
    }

    public void storeRefreshToken(String refreshToken, String username, Long duration) {
        redisUtil.setDataExpire(refreshToken, username, duration);
    }
}
