package com.BallaDream.BallaDream.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class JoinMailDto {
    @Email(message = "이메일 형식이어야 합니다.")
    private String username;
}
