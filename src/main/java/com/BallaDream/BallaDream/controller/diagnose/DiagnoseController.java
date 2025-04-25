package com.BallaDream.BallaDream.controller.diagnose;

import com.BallaDream.BallaDream.dto.diagnose.DiagnoseResultDto;
import com.BallaDream.BallaDream.dto.diagnose.TestDto;
import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.service.diagnose.DiagnoseService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiagnoseController {

    private final UserService userService;
    private final DiagnoseService diagnoseService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/diagnose")
    public ResponseEntity<ResponseDto> saveDiagnoseResult(@RequestBody DiagnoseResultDto resultDto) {
        log.info("받은 데이터 개수: {}", resultDto.getData().size());
        String username = userService.getUsernameInToken();
        diagnoseService.saveDiagnose(resultDto, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "사용자 피부 진단 정보를 성공적으로 저장하였습니다."));
    }
}
