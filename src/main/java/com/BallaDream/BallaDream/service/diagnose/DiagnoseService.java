package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.*;
import com.BallaDream.BallaDream.exception.diagnose.DiagnoseNotFoundException;
import com.BallaDream.BallaDream.exception.diagnose.DiagnoseOwnershipException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
import com.BallaDream.BallaDream.repository.diagnose.UserSkinLevelRepository;
import com.BallaDream.BallaDream.repository.diagnose.query.DiagnoseQueryRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;
    private final DiagnoseQueryRepository diagnoseQueryRepository;
    private final UserSkinLevelRepository levelRepository;
    private final UserRepository userRepository;

    //피부 진단 결과 저장
    //Todo diagnoseResult 에서 겹치는 DiagnoseType 있는지 점검하기
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

    //단일 진단 결과 조회
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

    //최근 진단 결과 조회
    public MyPageDiagnoseResponseDto getLatestDiagnose(Long userId) {

        Diagnose diagnose = diagnoseQueryRepository.findLatestDiagnoseByUser(userId); //사용자의 최근 진단 기록 조회
        if (diagnose == null) {
            throw new DiagnoseNotFoundException();
        }

        List<UserSkinLevel> skinLevelList = levelRepository.findByDiagnoseId(diagnose.getId());
        if (skinLevelList.isEmpty()) {
            throw new DiagnoseNotFoundException();
        }

        // DiagnosisType과 Level을 Map으로 매핑
        Map<DiagnoseType, Level> dataMap = skinLevelList.stream()
                .collect(Collectors.toMap(
                        UserSkinLevel::getDiagnoseType,
                        UserSkinLevel::getLevel));

        return new MyPageDiagnoseResponseDto(diagnose.getDate(), dataMap);
    }

    //사용자의 진단 기록을 모두 보내준다. (마이페이지 진단 이력)
    public UserAllDiagnoseResponseDto getAllDiagnose(Long userId, boolean isLatest) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(INVALID_USER));
        //when: 한페이지당 보여줄 데이터는 6개
        PageRequest pageRequest = getMyAllDiagnosePageRequest(isLatest);
        Page<Diagnose> page = diagnoseRepository.findByUser(user, pageRequest);
        List<Diagnose> content = page.getContent();

        //dto 만들기
        List<UserAllDiagnoseDto> data = new ArrayList<>();
        for (Diagnose diagnose : content) {
            List<UserSkinLevel> skinLevelList = diagnose.getSkinLevelList();
            Map<DiagnoseType, Level> diagnoseResult = new HashMap<>();
            for (UserSkinLevel sl : skinLevelList) {
                diagnoseResult.put(sl.getDiagnoseType(), sl.getLevel());
            }
            data.add(new UserAllDiagnoseDto(diagnose.getId(), diagnose.getDate(), diagnoseResult));
        }

        return UserAllDiagnoseResponseDto.builder()
                .data(data)
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }

    private PageRequest getMyAllDiagnosePageRequest(boolean isLatest) {
        //한페이지당 보여줄 데이터는 6개로 날짜를 기준으로 최신순으로 전송
        if (isLatest) {
            return PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "date"));
        }
        //날짜를 기준으로 가장 오래된 순으로 전송
        return PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "date"));
    }

    /*private List<UserAllDiagnoseDto> mapToUserAllDiagnoseDto(List<UserAllDiagnoseQueryDto> queryDtos) {
        return queryDtos.stream()
                // diagnoseId + date 기준으로 그룹핑
                .collect(Collectors.groupingBy(dto ->
                        new DiagnoseKey(dto.getDiagnoseId(), dto.getDate())))
                .entrySet().stream()
                .map(entry -> {
                    DiagnoseKey key = entry.getKey();
                    Map<DiagnoseType, Level> resultMap = entry.getValue().stream()
                            .collect(Collectors.toMap(
                                    UserAllDiagnoseQueryDto::getDiagnoseType,
                                    UserAllDiagnoseQueryDto::getLevel
                            ));

                    return UserAllDiagnoseDto.builder()
                            .diagnoseId(key.diagnoseId())
                            .diagnoseDate(key.diagnoseDate())
                            .diagnoseResult(resultMap)
                            .build();

                })
                .toList();
    }

    private record DiagnoseKey(
            Long diagnoseId,
            LocalDate diagnoseDate
    ) {}*/

}
