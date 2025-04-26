package com.BallaDream.BallaDream.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendProductDto {
    private Long productId;
    private String productName;
    private String formulation;
    private String price;
    private String salesLink;
    private String imageLink;
    private boolean isInterest;
    private List<String> element;
}
