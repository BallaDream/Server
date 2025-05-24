package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.user.JoinMailRequestDto;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user")
    public String test() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("name: {}", name);
        return "login user!";
    }

    @GetMapping("/")
    public String test2() {
        return "balladream :) ~^!^~";
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/user")
    public ResponseEntity<ResponseDto> deleteUser() {
        userService.softDeleteUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원이 탈퇴되었습니다."));
    }
}
