package com.BallaDream.BallaDream.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinRequestDto {
    @Email(message = "이메일 형식이어야 합니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;
    @NotBlank(message = "인증번호는 필수로 입력해야 합니다.")
    private String authNum; //인증번호
}
