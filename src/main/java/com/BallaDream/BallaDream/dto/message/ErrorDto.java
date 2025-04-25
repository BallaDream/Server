package com.BallaDream.BallaDream.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorDto {
    private int code;
    private String message;

    public static ErrorDto of(HttpStatus status, String message) {
        return new ErrorDto(status.value(), message);
    }
}
