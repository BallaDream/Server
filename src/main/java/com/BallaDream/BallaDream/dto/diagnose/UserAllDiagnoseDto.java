package com.BallaDream.BallaDream.dto.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class UserAllDiagnoseDto {
    private Long diagnoseId;
    private LocalDateTime diagnoseDate;
    private Map<DiagnoseType, Level> diagnoseResult = new HashMap<>();
}
