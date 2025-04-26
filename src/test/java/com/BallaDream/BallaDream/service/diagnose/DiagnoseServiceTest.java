package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.DiagnoseResultDto;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiagnoseServiceTest {

    @Autowired DiagnoseService diagnoseService;
    @Autowired UserRepository userRepository;
    @Autowired DiagnoseRepository diagnoseRepository;
    @Autowired UserSkinLevelRepository levelRepository;

    @Test
    @Transactional
    void saveDiagnosis() {

        //given
        DiagnoseResultDto dto = new DiagnoseResultDto();
        Map<DiagnosisType, Level> userSkinLevel = dto.getData(); //임의로 피부 진단 결과 생성
        userSkinLevel.put(DiagnosisType.DRY, Level.CAUTION);
        userSkinLevel.put(DiagnosisType.ACNE, Level.CLEAR);
        User user = new User();
        userRepository.save(user);

        //when
        diagnoseService.saveDiagnose(dto, user.getUsername());

        //then
        List<Diagnose> diagnoseResult = diagnoseRepository.findAll();
        List<UserSkinLevel> skinResult = levelRepository.findAll();
        assertThat(diagnoseResult.size()).isEqualTo(1); //진단 기록은 1개가 저장
        assertThat(skinResult.size()).isEqualTo(2); //피부 진단 결과는 2개가 저장
    }
}