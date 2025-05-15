package com.BallaDream.BallaDream.dto.diagnose;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

//진단 정보를 받을 때 사용하는 dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseSaveRequestDto {
//    private Map<DiagnoseType, Level> data = new HashMap<>();

    //각 세부 부위별 진단 결과
    @NotNull(message = "입술 건조도 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 4, message = "입술 건조도의 진단 결과는 0 ~ 4입니다.")
    private int dryLipsScore; //입술 건조도

    @NotNull(message = "이마 색소침착 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 5, message = "이마 색소침착의 진단 결과는 0 ~ 5입니다.")
    private int pigmentForeheadScore; //이마 색소침착
    @NotNull(message = "왼쪽볼 색소침착 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 7, message = "왼쪽볼 색소침착의 진단 결과는 0 ~ 7입니다.")
    private int pigmentLeftCheekScore; //왼쪽볼 색소침착
    @NotNull(message = "오른쪽볼 색소침착 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 7, message = "오른쪽볼 색소침착의 진단 결과는 0 ~ 7입니다.")
    private int pigmentRightCheekScore; //오른쪽볼 색소침착

    @NotNull(message = "이마 주름 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 8, message = "이마 주름의 진단 결과는 0 ~ 8입니다.")
    private int wrinkleForeheadScore; //이마 주름
    @NotNull(message = "미간 주름 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 6, message = "미간 주름의 진단 결과는 0 ~ 6입니다.")
    private int wrinkleGlabellaScore; //미간 주름
    @NotNull(message = "왼쪽 눈가 주름 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 6, message = "왼쪽 눈가 주름의 진단 결과는 0 ~ 6입니다.")
    private int wrinkleLeftEyeScore; //왼쪽 눈가 주름
    @NotNull(message = "오른쪽 눈가 주름 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 6, message = "오른쪽 눈가 주름의 진단 결과는 0 ~ 6입니다.")
    private int wrinkleRightEyeScore; //오른쪽 눈가 주름

    @NotNull(message = "왼쪽 볼 모공 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 5, message = "왼쪽 볼 모공의 진단 결과는 0 ~ 5입니다.")
    private int poreLeftCheekScore; //왼쪽 볼 모공
    @NotNull(message = "오른쪽 볼 모공 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 5, message = "오른쪽 볼 모공의 진단 결과는 0 ~ 5입니다.")
    private int poreRightCheekScore; //오른쪽 볼 모공

    @NotNull(message = "턱선 처짐 진단 결과를 입력해야 합니다.")
    @Range(min = 0, max = 6, message = "턱선 처짐의 진단 결과는 0 ~ 6입니다.")
    private int elasticJawlineSaggingScore; //턱선 처짐
}
