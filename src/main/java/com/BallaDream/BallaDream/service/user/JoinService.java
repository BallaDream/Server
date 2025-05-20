package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.user.JoinRequestDto;
import com.BallaDream.BallaDream.exception.user.DuplicateIdException;
import com.BallaDream.BallaDream.exception.user.InvalidMailAuthNumException;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //비밀번호 암호화
    private final MailSendService mailService;

    //웹 회원가입
    public void webJoinProcess(String username, String password, String authNum) {

        Boolean isExist = userRepository.existsByUsername(username);
        //이미 존재하는 아이디로 회원가입을 시도하는 경우 예외 발생
        if (isExist) {
            throw new DuplicateIdException();
        }

        boolean isCorrect = mailService.CheckAuthNum(username, authNum);
        //입력한 인증번호가 틀린 경우 예외처리
        if (!isCorrect) {
            throw new InvalidMailAuthNumException();
        }

        User newUser = new User(username, bCryptPasswordEncoder.encode(password), LoginType.WEB, UserRole.ROLE_USER);
        userRepository.save(newUser);
    }


    //카카오 회원가입 (controller 에서 유효성 검정을 한다)
    public User kakaoJoinProcess(String username) {
        User user = new User(username, null, LoginType.KAKAO, UserRole.ROLE_USER);
        return userRepository.save(user);
    }
}