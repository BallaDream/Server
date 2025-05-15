package com.BallaDream.BallaDream.dto.diagnose;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiagnoseSaveResponseDto {
    private int status;
    private Long diagnoseId;
    private String message;
}
