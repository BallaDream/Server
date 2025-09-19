package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.user.UpdateNicknameRequestDto;
import com.BallaDream.BallaDream.dto.user.UpdatePasswordRequestDto;
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
        return "login user!";
    }

    //사용자 닉네임 변경
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/user")
    public ResponseEntity<ResponseDto> updateNickname(@RequestBody @Validated UpdateNicknameRequestDto requestDto) {
        String username = userService.getUsernameInToken();
        userService.updateNickname(username, requestDto.getChangeNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원의 닉네임이 변경되었습니다."));
    }

    //사용자 비밀번호 변경
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/user/password")
    public ResponseEntity<ResponseDto> updatePassword(@RequestBody @Validated UpdatePasswordRequestDto requestDto) {
        userService.updatePassword(requestDto.getUsername(), requestDto.getPassword(), requestDto.getAuthNum());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원의 비밀번호가 변경되었습니다."));
    }


    //사용자 회원 탈퇴
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/user")
    public ResponseEntity<ResponseDto> deleteUser() {
        userService.softDeleteUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "회원이 탈퇴되었습니다."));
    }
}
