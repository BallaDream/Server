package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class DiagnoseRepositoryTest {

    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserSkinLevelRepository levelRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("사용자 진단 기록 생성")
    @Transactional
    void createDiagnoseWithSkinLevel() {

        //given
        User user = new User();
        userRepository.save(user);
        Diagnose diagnose = new Diagnose(user);
        diagnoseRepository.save(diagnose);
        UserSkinLevel skinLevel = new UserSkinLevel(diagnose, Level.CAUTION, DiagnoseType.ACNE);
        levelRepository.save(skinLevel);

        //when
        Diagnose result = diagnoseRepository.findById(diagnose.getId()).get();

        //then
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getSkinLevelList().get(0)).isEqualTo(skinLevel);
    }

    @Test
    @DisplayName("사용자의 진단 기록을 조회하기")
    @Transactional
    void checkUserDiagnose() {

        //given
        User user1 = new User();
        userRepository.save(user1);
        User user2 = new User();
        userRepository.save(user2);
        Diagnose diagnose = new Diagnose(user1);
        diagnoseRepository.save(diagnose);

        //when
        boolean isExist1 = diagnoseRepository.existsByUser(user1);
        boolean isExist2 = diagnoseRepository.existsByUser(user2);

        //then
        assertThat(isExist1).isTrue();
        assertThat(isExist2).isFalse();
    }
}