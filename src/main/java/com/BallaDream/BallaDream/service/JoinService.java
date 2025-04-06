package com.BallaDream.BallaDream.service;

import com.BallaDream.BallaDream.constants.LoginType;
import com.BallaDream.BallaDream.constants.UserRole;
import com.BallaDream.BallaDream.domain.UserEntity;
import com.BallaDream.BallaDream.dto.user.JoinDto;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //비밀번호 암호화

    //회원가입 로직
    public void joinProcess(JoinDto joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
//        data.setRole("ROLE_USER");
        data.setLoginType(LoginType.WEB);
        data.setRole(UserRole.ROLE_USER); //추가

        userRepository.save(data);
    }
}