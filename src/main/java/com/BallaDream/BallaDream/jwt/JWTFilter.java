package com.BallaDream.BallaDream.jwt;

import com.BallaDream.BallaDream.common.ResponseUtil;
import com.BallaDream.BallaDream.constants.UserRole;
import com.BallaDream.BallaDream.domain.UserEntity;
import com.BallaDream.BallaDream.dto.security.CustomUserDetails;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;


//jwt 를 검증하는 클래스
//OncePerRequestFilter 는 요청에 대해서 한번만 작동하는 필터
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    //토큰 인증 정보를 security context에 저장 (사용자 이름, 역할 등을 잠시 저장하려고)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = jwtUtil.resolveToken(httpServletRequest);

        try {
            //토큰이 유효 하다면
            if (StringUtils.hasText(jwt) && jwtUtil.isValidToken(jwt)) {
                String username = jwtUtil.getUsername(jwt);
                String role = jwtUtil.getRole(jwt);
                UserEntity userEntity = new UserEntity(username, role);
                //UserDetails 에 회원 정보 객체 담기
                CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
                //스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                //세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }

            filterChain.doFilter(request, response); //토큰이 필요없는 경우 일수도 있으므로 doFilter 수행

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            ResponseUtil.setErrorResponse(WRONG_TOKEN.getCode(), (HttpServletResponse) response, WRONG_TOKEN.getMessage()); //틀린 토큰 401
        } catch (ExpiredJwtException e) {
            ResponseUtil.setErrorResponse(EXPIRED_TOKEN.getCode(), (HttpServletResponse) response, EXPIRED_TOKEN.getMessage()); //토큰 만료 406
        } catch (UnsupportedJwtException e) {
            ResponseUtil.setErrorResponse(UNSUPPORTED_TOKEN.getCode(), (HttpServletResponse) response, UNSUPPORTED_TOKEN.getMessage()); //지원되지 않는 JWT 토큰
        } catch (IllegalArgumentException e) {
            ResponseUtil.setErrorResponse(ILLEGAL_TOKEN.getCode(), (HttpServletResponse) response, ILLEGAL_TOKEN.getMessage()); //JWT 토큰이 잘못됨
        }
    }
}
