package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.DiagnoseResultDto;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;
    private final UserSkinLevelRepository levelRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveDiagnose(DiagnoseResultDto resultDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(ResponseCode.INVALID_USER));

        Diagnose diagnose = new Diagnose(user);
        List<UserSkinLevel> userSkinLevelList = new ArrayList<>(); //저장할 피부 진단 결과 리스트
        Map<DiagnosisType, Level> diagnoseResult = resultDto.getData();
        //피부 진단 결과들에 대한 엔티티 생성
        diagnoseResult.forEach(((diagnosisType, level) -> {
            userSkinLevelList.add(new UserSkinLevel(diagnose, level, diagnosisType));
        }));

        diagnoseRepository.save(diagnose);
        levelRepository.saveAll(userSkinLevelList);
    }
}
