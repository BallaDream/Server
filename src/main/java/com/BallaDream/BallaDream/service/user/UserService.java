package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //security holder에 임시 저장된 사용자 정보를 반환한다.
    public String getUsernameInToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
