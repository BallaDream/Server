package com.BallaDream.BallaDream.jwt;

import com.BallaDream.BallaDream.common.CookieUtil;
import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.common.ResponseUtil;
import com.BallaDream.BallaDream.constants.RedisExpiredTime;
import com.BallaDream.BallaDream.constants.TokenType;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.security.CustomUserDetails;
import com.BallaDream.BallaDream.dto.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.BallaDream.BallaDream.constants.RedisExpiredTime.*;
import static com.BallaDream.BallaDream.constants.ResponseCode.INVALID_LOGIN_PARAMETER;
import static com.BallaDream.BallaDream.constants.ResponseCode.LOGIN_FAIL;
import static com.BallaDream.BallaDream.constants.TokenType.ACCESS_TOKEN;
import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;


/**
 * 스프링 시큐리티에서 로그인 검증은 authenticationManager 가 하기 때문에 username, password를 전달해야함.
 * 후에 로그인이 성공하였으면 토근 발급 & 실패했으면 특정 처리를 해준다.
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password를 가로챔
        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            UserDto userDto = objectMapper.readValue(messageBody, UserDto.class);

            //사용자가 아이디나 비밀번호를 입력하지 않은 경우
            if (userDto.getUsername() == null || userDto.getPassword() == null) {
                ResponseUtil.setErrorResponse(INVALID_LOGIN_PARAMETER.getCode(), response, INVALID_LOGIN_PARAMETER.getMessage());
                return null;
            }

            //spring security 전용 로그인 포맷 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(),
                    userDto.getPassword(), null);
            //authenticationManager에 사용자 아이디, 비밀번호 정보 넘기기 -> authManager가 정보를 userDetail에 전달한다.
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //로그인 성공시 실행하는 메서드 (jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        //유저 정보
//        String username = authentication.getName();
        //로그인 성공시 Authentication 객체가 생성된다.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        //회원이 탈퇴한 상태인 경우에는 bad request
        if (!user.isEnabled()) {
            //응답 설정
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("code", 400);
            errorBody.put("message", "탈퇴한 계정입니다. 로그인할 수 없습니다.");

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorBody));
            return ;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
//        String accessToken = jwtUtil.createJwt(ACCESS_TOKEN, username, role, jwtUtil.getAccessTokenExpiredTime());
        String accessToken = jwtUtil.createJwt(ACCESS_TOKEN, user.getUsername(), role, user.getNickname(),
                user.getLoginType(), jwtUtil.getAccessTokenExpiredTime());
        String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, user.getUsername(), role, user.getNickname(),
                user.getLoginType(), jwtUtil.getAccessTokenExpiredTime());

        //refresh 토큰 저장
        redisUtil.setDataExpire(refreshToken, user.getUsername(), jwtUtil.getRefreshTokenExpiredTime() / 1000);
        //빠른 조회를 위한 user_id 저장
        redisUtil.setDataExpire(user.getUsername(), String.valueOf(user.getId()), USER_CACHE_EXPIRE_SECONDS); // 1시간

        //응답 설정
        response.setHeader(ACCESS_TOKEN.getType(), accessToken); // 토큰 설정
        response.setHeader("Access-Control-Expose-Headers", ACCESS_TOKEN.getType());
        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.addCookie(CookieUtil.createCookie(REFRESH_TOKEN.getType(), refreshToken));
        response.setHeader("Set-Cookie" ,CookieUtil.createRefreshCookie(REFRESH_TOKEN.getType(), refreshToken).toString());
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.setErrorResponse(LOGIN_FAIL.getCode(), response, LOGIN_FAIL.getMessage());
    }
}
