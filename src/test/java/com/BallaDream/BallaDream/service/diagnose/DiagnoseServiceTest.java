package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.DiagnoseSaveRequestDto;
import com.BallaDream.BallaDream.dto.diagnose.UserAllDiagnoseResponseDto;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.BallaDream.BallaDream.domain.enums.DiagnoseType.DRY;
import static com.BallaDream.BallaDream.domain.enums.DiagnoseType.PIGMENT;
import static com.BallaDream.BallaDream.domain.enums.Level.CAUTION;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class DiagnoseServiceTest {

    @Autowired DiagnoseService diagnoseService;
    @Autowired UserRepository userRepository;
    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired UserSkinLevelRepository levelRepository;

    @Test
    @Transactional
    @DisplayName("사용자 진단 결과 저장하기")
    void saveDiagnosis() {

        //given

        //when

        //then
    }

    @Test
    @Transactional
    @DisplayName("사용자 모든 진단 기록 dto 양식 확인하기")
    void getAllDiagnose() {

        //given
        User user = new User();
        userRepository.save(user);
        //진단 1회
        Diagnose diagnose1 = new Diagnose();
        diagnose1.associateUser(user);
        diagnoseRepository.save(diagnose1);
        //진단 1회 결과
        UserSkinLevel l1 = new UserSkinLevel(diagnose1, CAUTION, DRY);
        UserSkinLevel l2 = new UserSkinLevel(diagnose1, CAUTION, PIGMENT);
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
        UserSkinLevel l4 = new UserSkinLevel(diagnose2, CAUTION, PIGMENT);
        l4.associateDiagnose(diagnose2);
        l4.associateDiagnose(diagnose2);
        levelRepository.save(l3);
        levelRepository.save(l4);

        //when
        UserAllDiagnoseResponseDto result = diagnoseService.getAllDiagnose(user.getId(), true, 0);

        //then
        log.info("{}", result); //response dto 양식 확인
        assertThat(result.getTotalPage()).isEqualTo(1);
    }
}