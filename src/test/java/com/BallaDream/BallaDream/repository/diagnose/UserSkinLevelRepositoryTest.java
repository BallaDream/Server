package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserSkinLevelRepositoryTest {

    @Autowired UserSkinLevelRepository levelRepository;
    @Autowired UserRepository userRepository;
    @Autowired DiagnoseRepository diagnoseRepository;

    @Test
    @Transactional
    void findByDiagnoseIdTest() {

        //given
        User user = new User();
        userRepository.save(user);
        Diagnose diagnose = new Diagnose();
        diagnose.associateUser(user);
        diagnoseRepository.save(diagnose);
        //2개 진단 결과 저장
        UserSkinLevel skinLevel1 = new UserSkinLevel(diagnose, Level.CAUTION, DiagnoseType.DRY);
        UserSkinLevel skinLevel2 = new UserSkinLevel(diagnose, Level.CAUTION, DiagnoseType.ACNE);
        levelRepository.save(skinLevel1);
        levelRepository.save(skinLevel2);

        //when
        List<UserSkinLevel> result = levelRepository.findByDiagnoseId(diagnose.getId());

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("카카오 로그인을 성공했는지 확인")
    @Transactional
    void kakaoLoginCheck() {

        //given
        User user = new User("hi", null, LoginType.KAKAO, UserRole.ROLE_USER);
        userRepository.save(user);

        //when
        Boolean result = userRepository.existsByUsernameAndLoginType("hi", LoginType.KAKAO);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("카카오 유저를 찾기")
    @Transactional
    void findKakaoUserByUsername() {

        //given
        User user = new User("hi", null, LoginType.KAKAO, UserRole.ROLE_USER);
        userRepository.save(user);

        //when
        Optional<User> findUser = userRepository.findByUsernameAndLoginType(user.getUsername(), LoginType.KAKAO);
        User result = findUser.get();
        //then
        assertThat(result).isEqualTo(user);
    }
}