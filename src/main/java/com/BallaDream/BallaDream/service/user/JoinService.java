package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.domain.enums.Action;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.user.JoinRequestDto;
import com.BallaDream.BallaDream.exception.user.DuplicateIdException;
import com.BallaDream.BallaDream.exception.user.InvalidMailAuthNumException;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import com.BallaDream.BallaDream.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.BallaDream.BallaDream.domain.enums.Action.*;
import static com.BallaDream.BallaDream.domain.enums.LoginType.*;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //비밀번호 암호화
    private final MailSendService mailService;
    private final LogService logService;

    //웹 회원가입
    public void webJoinProcess(String username, String password, String authNum) {

        Optional<User> findUser = userRepository.findByUsername(username);
        //이미 회원가입된 회원이고
        if (findUser.isPresent()) {
            User user = findUser.get();
            //계정이 활성화 되어 있는 계정이라면 메일 전송을 하지 않는다.
            if(user.isEnabled()){
                throw new DuplicateIdException();
            }
            //계정이 비활성화 된 경우, 활성화시키고, 비밀번호를 변경 시킨다.
            vertifyMailAuth(username, authNum);
            user.changePassword(bCryptPasswordEncoder.encode(password));
            reRegister(user);
            return;
        }

        vertifyMailAuth(username, authNum);
        createUser(username, bCryptPasswordEncoder.encode(password), WEB);
    }


    //카카오 회원가입 (kakao service 에서 유효성 검정을 한다)
    public User kakaoJoinProcess(String username) {
        return createUser(username, null, KAKAO);
    }

    //카카오 회원이 탈퇴했다가 다시 회원가입을 한 경우
    public void kakaoReRegister(User user) {
        reRegister(user);
    }

    //탈퇴 회원이 다시 회원가입을 한 경우
    private void reRegister(User user) {
        user.makeEnable();
        logService.recordUserLog(user.getId(), "탈퇴 회원이 다시 회원가입을 하였습니다.", this.getClass().getSimpleName(),
                RE_REGISTER);
    }

    private User createUser(String username, String password, LoginType loginType) {
        User newUser = new User(username, password, loginType, UserRole.ROLE_USER);
        return userRepository.save(newUser);
    }

    private void vertifyMailAuth(String username, String authNum) {
        boolean isCorrect = mailService.CheckAuthNum(username, authNum);
        //입력한 인증번호가 틀린 경우 예외처리
        if (!isCorrect) {
            throw new InvalidMailAuthNumException();
        }
    }
}