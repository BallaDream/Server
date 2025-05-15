package com.BallaDream.BallaDream.dto.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//사용자의 진단 정보를 조회할때 사용하는 dto
@Data
@AllArgsConstructor
public class UserDiagnoseResultResponseDto {

    private Map<String, Level> specificResult = new HashMap<>();
    private Map<DiagnoseType, Level> totalResult = new HashMap<>();
}
