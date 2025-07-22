package com.BallaDream.BallaDream.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNicknameRequestDto {
    @NotBlank(message = "변경할 닉네임을 입력해야 합니다")
    private String changeNickname;
}
