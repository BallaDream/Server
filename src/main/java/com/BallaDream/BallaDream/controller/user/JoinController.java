package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.user.JoinDto;
import com.BallaDream.BallaDream.dto.user.JoinMailDto;
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
    public ResponseEntity<ResponseDto> joinProcess(@RequestBody @Validated JoinDto joinDto) {

        joinService.webJoinProcess(joinDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원가입을 성공하였습니다"));
    }

    @PostMapping("/vertify-email")
    public ResponseEntity<ResponseDto> mailSendForJoin(@RequestBody @Validated JoinMailDto mailDto) {
        mailService.joinEmail(mailDto.getUsername()); //사용자에게 메일 전송
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "인증번호를 전송하였습니다"));
    }
}
