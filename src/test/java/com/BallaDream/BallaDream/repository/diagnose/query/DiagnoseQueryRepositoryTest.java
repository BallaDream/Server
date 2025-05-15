package com.BallaDream.BallaDream.repository.diagnose.query;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.UserAllDiagnoseQueryDto;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.BallaDream.BallaDream.domain.enums.DiagnoseType.*;
import static com.BallaDream.BallaDream.domain.enums.Level.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class DiagnoseQueryRepositoryTest {

    @Autowired DiagnoseQueryRepository diagnoseQueryRepository;
    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired UserSkinLevelRepository levelRepository;
    @Autowired UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("사용자의 모든 진단 기록을 가져오기")
    void getUserAllDiagnose() {

        //given
        User user = new User();
        userRepository.save(user);
        //진단 1회
        Diagnose diagnose1 = new Diagnose();
        diagnose1.associateUser(user);
        diagnoseRepository.save(diagnose1);
        //진단 1회 결과
        UserSkinLevel l1 = new UserSkinLevel(diagnose1, CAUTION, DRY);
        UserSkinLevel l2 = new UserSkinLevel(diagnose1, CAUTION, DRY);
        l1.associateDiagnose(diagnose1);
        l2.associateDiagnose(diagnose1);
        levelRepository.save(l1);
        levelRepository.save(l2);

        //진단 2회
        Diagnose diagnose2 = new Diagnose();
        diagnose2.associateUser(user);
        diagnoseRepository.save(diagnose2);
        //진단 2회 결과
        UserSkinLevel l3 = new UserSkinLevel(diagnose2, CAUTION, DRY);
        UserSkinLevel l4 = new UserSkinLevel(diagnose2, CAUTION, DRY);
        l4.associateDiagnose(diagnose2);
        l4.associateDiagnose(diagnose2);
        levelRepository.save(l3);
        levelRepository.save(l4);

        //when
        List<UserAllDiagnoseQueryDto> result = diagnoseQueryRepository.findAllDiagnoseByUser(user.getId());

        //then
        assertThat(result.size()).isEqualTo(4); //사용자의 진단결과는 총4개
    }
}