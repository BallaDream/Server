package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.user.AuthNumberCheckRequestDto;
import com.BallaDream.BallaDream.dto.user.JoinRequestDto;
import com.BallaDream.BallaDream.dto.user.JoinMailRequestDto;
import com.BallaDream.BallaDream.service.user.JoinService;
import com.BallaDream.BallaDream.service.user.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final MailSendService mailService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> joinProcess(@RequestBody @Validated JoinRequestDto joinRequestDto) {

        joinService.webJoinProcess(joinRequestDto.getUsername(), joinRequestDto.getPassword(), joinRequestDto.getAuthNum());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원가입을 성공하였습니다"));
    }

    @PostMapping("/join/auth-number/check")
    public ResponseEntity<ResponseDto> checkAuthNumber(@RequestBody @Validated AuthNumberCheckRequestDto checkRequestDto) {
        log.info("auth-check: {} {}", checkRequestDto.getUsername(), checkRequestDto.getAuthNum());
        boolean isChecked = mailService.CheckAuthNum(checkRequestDto.getUsername(), checkRequestDto.getAuthNum());
        if (isChecked) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseDto.of(HttpStatus.OK, "인증번호 인증에 성공하였습니다"));
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseDto.of(HttpStatus.FORBIDDEN, "유효하지 않은 인증번호 입니다"));
    }

    @PostMapping("/vertify-email")
    public ResponseEntity<ResponseDto> mailSendForJoin(@RequestBody @Validated JoinMailRequestDto mailDto) {
        mailService.joinEmail(mailDto.getUsername()); //사용자에게 메일 전송
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "인증번호를 전송하였습니다"));
    }
}
