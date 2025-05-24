package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.user.KakaoLoginResultDto;
import com.BallaDream.BallaDream.dto.user.KakaoTokenResponseDto;
import com.BallaDream.BallaDream.dto.user.KakaoUserInfoResponseDto;
import com.BallaDream.BallaDream.exception.user.AlreadyWebUserException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.jwt.JWTUtil;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.BallaDream.BallaDream.constants.RedisExpiredTime.USER_CACHE_EXPIRE_SECONDS;
import static com.BallaDream.BallaDream.constants.ResponseCode.DISABLED_USER;
import static com.BallaDream.BallaDream.constants.TokenType.ACCESS_TOKEN;
import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;
import static com.BallaDream.BallaDream.domain.enums.LoginType.KAKAO;
import static com.BallaDream.BallaDream.domain.enums.LoginType.WEB;
import static com.BallaDream.BallaDream.domain.enums.UserRole.ROLE_USER;

@Slf4j
@Service
public class KakaoService {
    private final String clientId;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    private final UserRepository userRepository;
    private final JoinService joinService;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public KakaoService(@Value("${kakao.client_id}") String clientId, UserRepository userRepository, JoinService joinService,
                        JWTUtil jwtUtil, RedisUtil redisUtil

    ) {
        this.clientId = clientId;
        this.userRepository = userRepository;
        this.joinService = joinService;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    public String getAccessTokenFromKakao(String code) {

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter1")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error1")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter2")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error2")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] email ---> {} ", userInfo.getKakaoAccount().getEmail());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    public KakaoLoginResultDto kakaoLogin(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);

        User user;
        //웹, 카카오 모두 회원가입이 되어 있지 않은 회원이면 카카오 회원가입을 진행한다.
        if (findUser.isEmpty()) {
            user = joinService.kakaoJoinProcess(username);
        } else {
            user = findUser.get();
            //웹 유저가 카카오로 로그인할 수 없음
            if (WEB.equals(user.getLoginType())) {
                throw new AlreadyWebUserException();
            }
            //회원 탈퇴한 계정으로 다시 로그인할 경우, 계정을 활성화시킨다.
            else if (!user.isEnabled()) {
                joinService.kakaoReRegister(user);
            }
        }
        String accessToken = jwtUtil.createJwt(ACCESS_TOKEN, username, ROLE_USER.getUserRoleType(), user.getNickname(),
                KAKAO, jwtUtil.getAccessTokenExpiredTime());
        String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, username, ROLE_USER.getUserRoleType(), user.getNickname(),
                KAKAO, jwtUtil.getAccessTokenExpiredTime());

        //refresh 토큰 저장
        redisUtil.setDataExpire(refreshToken, user.getUsername(), jwtUtil.getRefreshTokenExpiredTime() / 1000);
        //빠른 조회를 위한 user_id 저장
        redisUtil.setDataExpire(user.getUsername(), String.valueOf(user.getId()), USER_CACHE_EXPIRE_SECONDS); // 1시간

        return new KakaoLoginResultDto(accessToken, refreshToken);
    }

    /**
     * 카카오 회원의 유효성을 검증한다.
     * 카카오 회원으로 로그인을 시도했으나, 이미 웹으로 회원가입을 했으면 거절 처리한다.
     */

    public void isNotValidKakaoUser(String username) {

    }
}
