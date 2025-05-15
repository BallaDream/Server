package com.BallaDream.BallaDream.dto.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeProductRequestDto {
    @NotBlank(message = "상품 아이디는 필수로 입력해야 합니다.")
    private Long productId;
    @NotBlank(message = "추천 종류를 필수로 입력해야 합니다.")
    private DiagnoseType diagnoseType;
}
