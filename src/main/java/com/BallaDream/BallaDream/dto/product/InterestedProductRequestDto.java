package com.BallaDream.BallaDream.dto.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestedProductRequestDto {
    @NotNull(message = "상품 정보는 필수로 입력해야 합니다.")
    private Long productId;
    @NotNull(message = "상품의 진단 종류는 필수로 입력해야 합니다.")
    private DiagnoseType diagnoseType;
}
