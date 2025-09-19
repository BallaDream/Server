package com.BallaDream.BallaDream.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequestDto {
    @Email(message = "이메일 형식이어야 합니다.")
    private String username;

    @Size(min = 8, max = 30, message = "비밀번호는 8자리 ~ 30자리 이어야 합니다.")
    @NotBlank(message = "변경할 비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "인증번호는 필수로 입력해야 합니다.")
    private String authNum; //인증번호
}
