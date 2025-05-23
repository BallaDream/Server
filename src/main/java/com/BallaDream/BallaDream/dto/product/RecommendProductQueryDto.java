package com.BallaDream.BallaDream.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class RecommendProductQueryDto {
    private Long productId;
    private String productName;
    private String formulation;
    private int price;
    private String salesLink;
    private String imageLink;
    private String elementName;
    private boolean isInterested;

    @QueryProjection

    public RecommendProductQueryDto(Long productId, String productName, String formulation, int price, String salesLink, String imageLink, String elementName, boolean isInterested) {
        this.productId = productId;
        this.productName = productName;
        this.formulation = formulation;
        this.price = price;
        this.salesLink = salesLink;
        this.imageLink = imageLink;
        this.elementName = elementName;
        this.isInterested = isInterested;
    }
}
