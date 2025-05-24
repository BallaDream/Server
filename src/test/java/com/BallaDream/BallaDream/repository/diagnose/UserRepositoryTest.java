package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired EntityManager em;

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

    @Test
    @DisplayName("사용자의 pk 회원 탈퇴하기")
    @Transactional
    void deleteUserById() {

        //given
        User user = new User("hi", null, LoginType.KAKAO, UserRole.ROLE_USER);
        User savedUser = userRepository.save(user);

        //when
        userRepository.softDeleteUser(savedUser.getId());
        em.flush();
        em.clear();
        User result = userRepository.findById(savedUser.getId()).get();

        //then
        assertThat(result.isEnabled()).isFalse(); //계정이 비활성화 되어 있는가
        assertThat(result.getDeletedAt()).isEqualTo(LocalDate.now()); //삭제 시간 확인
    }
}