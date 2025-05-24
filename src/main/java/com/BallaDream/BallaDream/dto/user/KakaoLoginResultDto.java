package com.BallaDream.BallaDream.dto.user;

import lombok.Getter;

@Getter
public class KakaoLoginResultDto {
    private final String accessToken;
    private final String refreshToken;

    public KakaoLoginResultDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
