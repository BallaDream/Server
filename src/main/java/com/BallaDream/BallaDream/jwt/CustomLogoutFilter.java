package com.BallaDream.BallaDream.jwt;

import com.BallaDream.BallaDream.common.CookieUtil;
import com.BallaDream.BallaDream.common.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.BallaDream.BallaDream.constants.ResponseCode.EXPIRED_TOKEN;
import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;


@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //로그아웃 경로가 아니거나 post 요청이 아니면 패스
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.matches("^\\/logout$") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refreshToken = jwtUtil.getRefreshToken(request.getCookies());
        String category = jwtUtil.getCategory(refreshToken); //refresh 토큰인가
        //refresh valid check
        if (refreshToken == null || !category.equals(REFRESH_TOKEN.getType())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            //response status code
            response.setStatus(EXPIRED_TOKEN.getCode());
            return;
        }

        //DB에 저장되어 있는지 확인
        String getRefreshToken = redisUtil.getData(refreshToken);
        //refresh token이 redis에 없는 경우
        if (!StringUtils.hasText(getRefreshToken)) {
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        redisUtil.deleteData(refreshToken);

        //Refresh 토큰 유효시간을 0으로 하여 삭제하기
        Cookie cookie = CookieUtil.deleteRefreshTokenInCookie();
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
