package com.BallaDream.BallaDream.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationProductResponseDto {
    private List<RecommendProductDto> data;
}
