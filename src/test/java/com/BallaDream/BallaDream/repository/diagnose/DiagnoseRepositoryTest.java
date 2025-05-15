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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class DiagnoseRepositoryTest {

    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserSkinLevelRepository levelRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("사용자 진단 기록 생성 및 조회")
    @Transactional
    void createDiagnoseWithSkinLevel() {

        //given
        User user = new User();
        userRepository.save(user);
        Diagnose diagnose = new Diagnose();
        diagnose.associateUser(user);
        Diagnose savedDiagnose = diagnoseRepository.save(diagnose);

        //when
        Diagnose result = diagnoseRepository.findById(savedDiagnose.getId()).get();

        //then
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result).isEqualTo(diagnose);
    }

    @Test
    @DisplayName("사용자의 진단 기록이 있는지 확인하기")
    @Transactional
    void checkUserDiagnose() {

        //given
        User user1 = new User();
        userRepository.save(user1);
        User user2 = new User();
        userRepository.save(user2);
        Diagnose diagnose = new Diagnose();
        diagnose.associateUser(user1);
        diagnoseRepository.save(diagnose);

        //when
        boolean isExist1 = diagnoseRepository.existsByUser(user1);
        boolean isExist2 = diagnoseRepository.existsByUser(user2);

        //then
        assertThat(isExist1).isTrue();
        assertThat(isExist2).isFalse();
    }

    @Test
    @DisplayName("사용자의 pk 와 진단 기록의 pk로 데이터 조회하기")
    @Transactional
    void findByDiagnoseIdAndUserId() {

        //given
        User user = new User();
        User savedUser = userRepository.save(user);
        Diagnose diagnose = new Diagnose();
        diagnose.associateUser(user);
        Diagnose savedDiagnose = diagnoseRepository.save(diagnose);

        //when
        Diagnose result = diagnoseRepository.findByIdAndUserId(savedDiagnose.getId(), savedUser.getId()).get();

        //then
        assertThat(result).isEqualTo(diagnose);
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("사용자의 모든 진단 기록을 조회하기")
    @Transactional
    void pagingUserDiagnose() {

        //given
        User user = new User();
        userRepository.save(user);
        //3개의 진단 기록 저장
        Diagnose diagnose1 = new Diagnose();
        diagnose1.associateUser(user);
        diagnoseRepository.save(diagnose1);
        Diagnose diagnose2 = new Diagnose();
        diagnose2.associateUser(user);
        diagnoseRepository.save(diagnose2);
        Diagnose diagnose3 = new Diagnose();
        diagnose3.associateUser(user);
        diagnoseRepository.save(diagnose3);
        //진단 기록에 따른 피부 등급 저장
        UserSkinLevel skinLevel1 = new UserSkinLevel(diagnose1, Level.CAUTION, DiagnoseType.ACNE);
        skinLevel1.associateDiagnose(diagnose1);
        levelRepository.save(skinLevel1);
        UserSkinLevel skinLevel2 = new UserSkinLevel(diagnose2, Level.CLEAR, DiagnoseType.DRY);
        skinLevel2.associateDiagnose(diagnose2);
        levelRepository.save(skinLevel2);
        UserSkinLevel skinLevel3 = new UserSkinLevel(diagnose3, Level.WARNING, DiagnoseType.PIGMENT);
        skinLevel3.associateDiagnose(diagnose3);
        levelRepository.save(skinLevel3);
        em.flush();
        em.clear();

        //when: 한페이지당 보여줄 데이터는 6개 + 최신순으로
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "date"));
        Page<Diagnose> page = diagnoseRepository.findByUser(user, pageRequest);

        //then
        List<Diagnose> result = page.getContent();
        for (Diagnose diagnose : result) {
            List<UserSkinLevel> skinLevelList = diagnose.getSkinLevelList();
            for (UserSkinLevel skinLevel : skinLevelList) {
                log.info("data: {}", skinLevel.getDiagnoseType());
            }
        }
    }
}