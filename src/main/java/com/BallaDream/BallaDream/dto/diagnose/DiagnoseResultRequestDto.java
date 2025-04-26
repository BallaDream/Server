package com.BallaDream.BallaDream.dto.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

//진단 정보를 받을 때 사용하는 dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseResultRequestDto {
    private Map<DiagnoseType, Level> data = new HashMap<>();
}
