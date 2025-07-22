package com.BallaDream.BallaDream.controller.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.dto.diagnose.*;
import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.mypage.MyPageDiagnoseResponseDto;
import com.BallaDream.BallaDream.service.diagnose.DiagnoseService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
    public DiagnoseSaveResponseDto saveDiagnoseResult(@RequestBody @Validated DiagnoseSaveRequestDto resultDto) {
        String username = userService.getUsernameInToken();
        Diagnose saveDiagnose = diagnoseService.saveDiagnose(resultDto, username);
        return new DiagnoseSaveResponseDto(HttpStatus.OK.value(), saveDiagnose.getId(), "피부 진단 결과가 저장되었습니다");
    }

    //사용자의 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/diagnose/{id}")
    public UserDiagnoseResultResponseDto getDiagnosisResult(@PathVariable(name = "id") Long id) {
        return diagnoseService.getUserDiagnose(userService.getUserId(), id);
    }

    //마이 페이지에서 사용자의 가장 최근의 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage/diagnose")
    public MyPageDiagnoseResponseDto getLatestDiagnose() {
        return diagnoseService.getLatestDiagnose(userService.getUserId());
    }

    //마이 페이지에서 사용자의 모든 피부 진단 기록을 반환한다.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage/diagnoses")
    public UserAllDiagnoseResponseDto getALLDiagnose(@RequestParam(required = false, defaultValue = "true") boolean isLatest,
                                                     @RequestParam(required = false, defaultValue = "0") int page) {
        return diagnoseService.getAllDiagnose(userService.getUserId(), isLatest, page);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/diagnose/{diagnoseId}")
    public ResponseEntity<ResponseDto> deleteDiagnose(@PathVariable Long diagnoseId) {
        diagnoseService.deleteDiagnose(diagnoseId, userService.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "해당 피부 진단 내용을 삭제했습니다."));
    }
}
