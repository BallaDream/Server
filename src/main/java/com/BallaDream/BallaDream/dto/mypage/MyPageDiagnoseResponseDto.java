package com.BallaDream.BallaDream.dto.mypage;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class MyPageDiagnoseResponseDto {
    private Long diagnoseId;
    private LocalDate diagnoseDate;
    private Map<DiagnoseType, Level> data = new HashMap<>();
}
