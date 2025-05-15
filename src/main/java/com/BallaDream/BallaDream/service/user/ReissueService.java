package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.exception.token.ExpiredRefreshTokenException;
import com.BallaDream.BallaDream.exception.token.InvalidRefreshTokenException;
import com.BallaDream.BallaDream.exception.token.RefreshTokenNotFoundException;
import com.BallaDream.BallaDream.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.BallaDream.BallaDream.constants.TokenType.ACCESS_TOKEN;
import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public String reissueAccessToken(HttpServletRequest request) {

        // 쿠키에서 Refresh Token 추출
        String refresh = getTokenInCookie(request);

        if (refresh == null) {
            throw new RefreshTokenNotFoundException();
        }

        String getRefreshTokenInRedis = redisUtil.getData(refresh); //redis 에 저장되어 있는 토큰인지 확인
        if (getRefreshTokenInRedis == null){
            throw new InvalidRefreshTokenException();
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException();
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals(REFRESH_TOKEN.getType())) {
            throw new InvalidRefreshTokenException();
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String nickname = jwtUtil.getNickname(refresh);
        LoginType loginType = jwtUtil.getLoginType(refresh);

        // 새로운 Access Token 발급
        return jwtUtil.createJwt(ACCESS_TOKEN, username, role,nickname,loginType, jwtUtil.getAccessTokenExpiredTime());
    }

    public String getTokenInCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN.getType())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
