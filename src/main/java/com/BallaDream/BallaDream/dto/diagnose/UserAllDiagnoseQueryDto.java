package com.BallaDream.BallaDream.dto.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserAllDiagnoseQueryDto {
    private Long diagnoseId;
    private LocalDate date;
    private Level level;
    private DiagnoseType diagnoseType;

    @QueryProjection

    public UserAllDiagnoseQueryDto(Long diagnoseId, LocalDate date, Level level, DiagnoseType diagnoseType) {
        this.diagnoseId = diagnoseId;
        this.date = date;
        this.level = level;
        this.diagnoseType = diagnoseType;
    }
}
