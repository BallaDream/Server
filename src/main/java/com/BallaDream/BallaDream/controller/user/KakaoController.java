package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.common.CookieUtil;
import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.user.KakaoLoginResultDto;
import com.BallaDream.BallaDream.dto.user.KakaoUserInfoResponseDto;
import com.BallaDream.BallaDream.exception.user.AlreadyWebUserException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.jwt.JWTUtil;
import com.BallaDream.BallaDream.service.user.JoinService;
import com.BallaDream.BallaDream.service.user.KakaoService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.BallaDream.BallaDream.constants.RedisExpiredTime.USER_CACHE_EXPIRE_SECONDS;
import static com.BallaDream.BallaDream.constants.ResponseCode.*;
import static com.BallaDream.BallaDream.constants.TokenType.ACCESS_TOKEN;
import static com.BallaDream.BallaDream.constants.TokenType.REFRESH_TOKEN;
import static com.BallaDream.BallaDream.domain.enums.LoginType.*;
import static com.BallaDream.BallaDream.domain.enums.UserRole.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final JoinService joinService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final JWTUtil jwtUtil;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    //test용 카카오 로그인 화면
    @GetMapping("/kakao/login")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        model.addAttribute("location", location);
        return "login";
    }

    //kakao 로그인
    @GetMapping("/kakao/authenticate")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String kakaoLoginToken = kakaoService.getAccessTokenFromKakao(code); //카카오로 부터 로그인 토큰 가져오기
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoLoginToken); //kakao에 인증 요청
        String username = userInfo.getKakaoAccount().getEmail();
        KakaoLoginResultDto result = kakaoService.kakaoLogin(username);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        responseBody.put("message", "카카오 로그인 성공");

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN.getType(), result.getAccessToken());
        headers.set("Access-Control-Expose-Headers", ACCESS_TOKEN.getType());
        headers.add("Set-Cookie", CookieUtil.createCookie(REFRESH_TOKEN.getType(), result.getRefreshToken()).toString());

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }
}
