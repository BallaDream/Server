package com.BallaDream.BallaDream.dto.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendProductQueryDto {
    private Boolean hasNext;
    private List<RecommendProductQueryContent> content;
}
