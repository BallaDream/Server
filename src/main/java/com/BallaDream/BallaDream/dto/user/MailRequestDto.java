package com.BallaDream.BallaDream.dto.user;

import com.BallaDream.BallaDream.constants.EmailAuthNumberType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailRequestDto {
    @Email(message = "이메일 형식이어야 합니다.")
    private String username;

    @NotNull(message = "인증 번호 용도를 설정해야 합니다.")
    private EmailAuthNumberType authNumberType;
}
