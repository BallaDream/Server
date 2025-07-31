package com.BallaDream.BallaDream.service.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.diagnose.*;
import com.BallaDream.BallaDream.dto.mypage.MyPageDiagnoseResponseDto;
import com.BallaDream.BallaDream.exception.diagnose.DiagnoseNotFoundException;
import com.BallaDream.BallaDream.exception.diagnose.DiagnoseOwnershipException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.diagnose.DiagnoseRepository;
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

import static com.BallaDream.BallaDream.constants.DiagnoseScore.*;
import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;
    private final DiagnoseQueryRepository diagnoseQueryRepository;
    private final UserRepository userRepository;

    //진단 기록 저장하기
    @Transactional
    public Diagnose saveDiagnose(DiagnoseSaveRequestDto dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(INVALID_USER));

        Diagnose diagnose = Diagnose
                .builder()
                .user(user)
                .dryLipsLevel(DRY_LIPS_SCORE.getLevelByScore(dto.getDryLipsScore())) //입술 건조도
                .pigmentForeheadLevel(PIGMENT_FOREHEAD_SCORE.getLevelByScore(dto.getPigmentForeheadScore())) //이마 색소침착
                .pigmentLeftCheekLevel(PIGMENT_CHEEK_SCORE.getLevelByScore(dto.getPigmentLeftCheekScore())) //왼쪽볼 색소침착
                .pigmentRightCheekLevel(PIGMENT_CHEEK_SCORE.getLevelByScore(dto.getPigmentRightCheekScore())) //오른쪽볼 색소침착
                .wrinkleForeheadLevel(WRINKLE_FOREHEAD_SCORE.getLevelByScore(dto.getWrinkleForeheadScore())) //이마 주름
                .wrinkleGlabellaLevel(WRINKLE_GLABELLA_SCORE.getLevelByScore(dto.getWrinkleGlabellaScore())) //미간 주름
                .wrinkleLeftEyeLevel(WRINKLE_EYE_SCORE.getLevelByScore(dto.getWrinkleLeftEyeScore())) //왼쪽 눈가 주름
                .wrinkleRightEyeLevel(WRINKLE_EYE_SCORE.getLevelByScore(dto.getWrinkleRightEyeScore())) //오른쪽 눈가 주름
                .poreLeftCheekLevel(PORE_CHEEK_SCORE.getLevelByScore(dto.getPoreLeftCheekScore())) //왼쪽 볼 모공
                .poreRightCheekLevel(PORE_CHEEK_SCORE.getLevelByScore(dto.getPoreRightCheekScore())) //오른쪽 볼 모공
                .elasticJawlineSaggingLevel(ELASTIC_JAWLINE_SAGGING_SCORE.getLevelByScore(dto.getElasticJawlineSaggingScore())) //턱선처짐
                .build();

        diagnose.createDate(); //날짜 기입
        diagnose.associateUser(user); //연관 관계 셋팅
        return diagnoseRepository.save(diagnose);
    }

    //단일 진단 결과 조회
    public UserDiagnoseResultResponseDto getUserDiagnose(Long userId, Long diagnoseId) {

        Optional<Diagnose> findDiagnose = diagnoseRepository.findByIdAndUserId(diagnoseId, userId);

        //열람하고자 하는 기록이 사용자의 진단 기록이 아닌 경우
        if (findDiagnose.isEmpty()) {
            throw new DiagnoseOwnershipException();
        }

        Diagnose diagnose = findDiagnose.get();
        return new UserDiagnoseResultResponseDto(diagnose.getSpecificUserSkinLevel(), diagnose.getTotalUserSkinLevel());
    }

    //최근 진단 결과 조회
    public MyPageDiagnoseResponseDto getLatestDiagnose(Long userId) {

        Diagnose diagnose = diagnoseQueryRepository.findLatestDiagnoseByUser(userId); //사용자의 최근 진단 기록 조회
        if (diagnose == null) {
            throw new DiagnoseNotFoundException();
        }

        return new MyPageDiagnoseResponseDto(diagnose.getId(), diagnose.getDate(), diagnose.getTotalUserSkinLevel());
    }

    //사용자의 진단 기록을 모두 보내준다. (마이페이지 진단 이력)
    public UserAllDiagnoseResponseDto getAllDiagnose(Long userId, boolean isLatest, int page) {

        //when: 한페이지당 보여줄 데이터는 6개
        PageRequest pageRequest = getMyAllDiagnosePageRequest(isLatest, page);
        Page<Diagnose> pageResult = diagnoseRepository.findByUserId(userId, pageRequest);
        List<Diagnose> content = pageResult.getContent();

        //dto 만들기
        List<UserAllDiagnoseDto> data = new ArrayList<>();
        for (Diagnose diagnose : content) {
            data.add(new UserAllDiagnoseDto(diagnose.getId(), diagnose.getDate(), diagnose.getTotalUserSkinLevel()));
        }

        return UserAllDiagnoseResponseDto.builder()
                .data(data)
                .totalCount(pageResult.getTotalElements())
                .totalPage(pageResult.getTotalPages())
                .currentPage(pageResult.getNumber())
                .build();
    }

    private PageRequest getMyAllDiagnosePageRequest(boolean isLatest, int page) {
        //한페이지당 보여줄 데이터는 6개로 날짜를 기준으로 최신순으로 전송
        if (isLatest) {
            return PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "date"));
        }
        //날짜를 기준으로 가장 오래된 순으로 전송
        return PageRequest.of(page, 6, Sort.by(Sort.Direction.ASC, "date"));
    }

    //피부 진단 결과 삭제하기
    @Transactional
    public void deleteDiagnose(Long diagnoseId, Long userId) {
        diagnoseRepository.deleteByIdAndUserId(diagnoseId, userId);
    }
}
