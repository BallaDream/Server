package com.BallaDream.BallaDream.controller.diagnose;

import com.BallaDream.BallaDream.dto.diagnose.DiagnoseResultRequestDto;
import com.BallaDream.BallaDream.dto.diagnose.MyPageDiagnoseResponseDto;
import com.BallaDream.BallaDream.dto.diagnose.UserAllDiagnoseResponseDto;
import com.BallaDream.BallaDream.dto.diagnose.UserDiagnoseResultResponseDto;
import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.service.diagnose.DiagnoseService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiagnoseController {

    private final UserService userService;
    private final DiagnoseService diagnoseService;

    //사용자의 피부 진단 기록을 저장함
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/diagnose")
    public ResponseEntity<ResponseDto> saveDiagnoseResult(@RequestBody DiagnoseResultRequestDto resultDto) {
        String username = userService.getUsernameInToken();
        diagnoseService.saveDiagnose(resultDto.getData(), username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "사용자 피부 진단 정보를 성공적으로 저장하였습니다."));
    }

    //사용자의 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/diagnose/{id}")
    public UserDiagnoseResultResponseDto getDiagnosisResult(@PathVariable(name = "id") Long id) {
        String username = userService.getUsernameInToken();
        return diagnoseService.getUserDiagnose(id, username);
    }

    //사용자의 가장 최근의 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage/diagnose")
    public MyPageDiagnoseResponseDto getLatestDiagnose() {
        Long userId = userService.getUserId();
        return diagnoseService.getLatestDiagnose(userId);
    }

    //사용자의 모든 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage/diagnoses")
    public UserAllDiagnoseResponseDto getALLDiagnose() {
        Long userId = userService.getUserId();
        return diagnoseService.getAllDiagnose(userId, true); //Todo 최신 여부를 입력받고 반환하기
    }

}
