package com.BallaDream.BallaDream.jwt;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.constants.TokenType;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;
import static com.BallaDream.BallaDream.constants.TokenType.*;

@Getter
@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpiredTime;
    private final Long refreshTokenExpiredTime;
    private final UserRepository userRepository;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.access-token-validity-time}") Long accessTokenExpiredTime,
                   @Value("${spring.jwt.refresh-token-validity-time}") Long refreshTokenExpiredTime,
                   UserRepository userRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpiredTime = accessTokenExpiredTime;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
        this.userRepository = userRepository;
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getNickname(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public LoginType getLoginType(String token) {
        String loginTypeStr = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginType", String.class);
        return LoginType.valueOf(loginTypeStr);
    }



    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //토큰이 access 용인가, refresh 용도인가 판단
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createJwt(TokenType token, String username, String role, String nickname, LoginType loginType, Long expiredMs) {

        return Jwts.builder()
                .claim("category", token.getType()) //refresh or access
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("loginType", loginType)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    //쿠키에서 refresh 토큰을 꺼내는 메서드
    public String getRefreshToken(Cookie[] cookies) {

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN.getType())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    //토큰이 유효한지 테스트
    public boolean isValidToken(String token) {
        String username = getUsername(token);
        String role = getRole(token);

        if (isExpired(token) || username == null || role == null) {
            return false;
        }
        return true;
    }

    //Request Header 에서 토큰 정보를 꺼내오기 위한 메서드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); //bearer 제외한 실제 토큰 값만 반환
        }

        return null;
    }
}
