package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.BallaDream.BallaDream.constants.RedisExpiredTime.*;
import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    //security holder에 임시 저장된 사용자 정보를 반환한다.
    public String getUsernameInToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // username을 기반으로 userId 가져오기
    public Long getUserId() {
        String username = getUsernameInToken();
        String userIdStr = redisUtil.getData(username);

        if (userIdStr != null) {
            return Long.parseLong(userIdStr);
        }

        // Redis에 없으면 DB에서 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(INVALID_USER));

        // 조회한 결과를 Redis에 다시 저장
        redisUtil.setDataExpire(username, String.valueOf(user.getId()), USER_CACHE_EXPIRE_SECONDS); // 1시간

        return user.getId();
    }
}
