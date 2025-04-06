package com.BallaDream.BallaDream.jwt;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JWTUtilTest {

    @Autowired JWTUtil jwtUtil;

    @Test
    void test() {
        Long accessTime = jwtUtil.getAccessTokenExpiredTime();
        Long refreshTime = jwtUtil.getRefreshTokenExpiredTime();
        log.info("{}, {}", accessTime, refreshTime);
    }
}