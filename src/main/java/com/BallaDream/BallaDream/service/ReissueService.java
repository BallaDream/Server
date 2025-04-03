package com.BallaDream.BallaDream.service;

import com.BallaDream.BallaDream.common.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final RedisUtil redisUtil;

    //redis에 refresh 토큰이 저장됐는지 체크
    public Boolean isExist(String refreshToken) {
        return StringUtils.hasText(redisUtil.getData(refreshToken));
    }

    //logout 시 refresh 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {
        redisUtil.deleteData(refreshToken);
    }

    public void storeRefreshToken(String refreshToken, String username, Long duration) {
        redisUtil.setDataExpire(refreshToken, username, duration);
    }
}
