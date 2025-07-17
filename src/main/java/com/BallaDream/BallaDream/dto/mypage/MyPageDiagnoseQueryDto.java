package com.BallaDream.BallaDream.dto.mypage;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MyPageDiagnoseQueryDto {
    private LocalDate diagnoseDate;
    private DiagnoseType diagnosisType;
    private Level level;

    @QueryProjection
    public MyPageDiagnoseQueryDto(LocalDate diagnoseDate, DiagnoseType diagnosisType, Level level) {
        this.diagnoseDate = diagnoseDate;
        this.diagnosisType = diagnosisType;
        this.level = level;
    }
}
