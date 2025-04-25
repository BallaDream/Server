package com.BallaDream.BallaDream.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

//단순히 성공 응답에 대한 dto
@Data
@AllArgsConstructor
public class ResponseDto {
    private int status;
    private String message;
}
