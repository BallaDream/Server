package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.DiagnoseResultRequestDto;
import com.BallaDream.BallaDream.dto.diagnose.UserDiagnoseResultResponseDto;
import com.BallaDream.BallaDream.exception.user.DiagnoseOwnershipException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;
    private final UserSkinLevelRepository levelRepository;
    private final UserRepository userRepository;

    //피부 진단 결과 저장
    @Transactional
    public void saveDiagnose(Map<DiagnoseType, Level> diagnoseResult, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(INVALID_USER));

        Diagnose diagnose = new Diagnose(user);
        diagnose.associateUser(user);
        List<UserSkinLevel> userSkinLevelList = new ArrayList<>(); //저장할 피부 진단 결과 리스트
        //피부 진단 결과들에 대한 엔티티 생성
        diagnoseResult.forEach(((diagnosisType, level) -> {
            UserSkinLevel skinLevel = new UserSkinLevel(diagnose, level, diagnosisType);
            skinLevel.associateDiagnose(diagnose);
            userSkinLevelList.add(skinLevel);
        }));

        diagnoseRepository.save(diagnose);
        levelRepository.saveAll(userSkinLevelList);
    }

    public UserDiagnoseResultResponseDto getUserDiagnose(Long diagnoseId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(INVALID_USER));

        List<Diagnose> diagnoseList = diagnoseRepository.findByUser(user); //사용자의 진단 기록 조회
        boolean isUserDiagnose = false;
        for (Diagnose diagnose : diagnoseList) {
            if (Objects.equals(diagnose.getId(), diagnoseId)) {
                isUserDiagnose = true;
                break;
            }
        }
        //열람하고자 하는 기록이 사용자의 진단 기록이 아닌 경우
        if (!isUserDiagnose) {
            throw new DiagnoseOwnershipException();
        }

        List<UserSkinLevel> skinLevelList = levelRepository.findByDiagnoseId(diagnoseId);
        Map<DiagnoseType, Level> result = new HashMap<>();
        for (UserSkinLevel skinLevel : skinLevelList) {
            result.put(skinLevel.getDiagnoseType(), skinLevel.getLevel());
        }

        return new UserDiagnoseResultResponseDto(result);
    }
}
