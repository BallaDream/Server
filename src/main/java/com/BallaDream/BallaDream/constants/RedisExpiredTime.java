package com.BallaDream.BallaDream.constants;

//redis 에 들어갈 데이터의 제한시간을 명시
public class RedisExpiredTime {
    public static final long USER_CACHE_EXPIRE_SECONDS = 60 * 60L; // 1시간
    public static final long REFRESH_TOKEN_EXPIRE_SECONDS = 24 * 60 * 60L; // 1시간
}
