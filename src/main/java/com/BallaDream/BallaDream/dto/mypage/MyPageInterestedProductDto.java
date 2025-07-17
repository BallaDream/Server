package com.BallaDream.BallaDream.dto.mypage;

import lombok.Data;

import java.util.List;

@Data
public class MyPageInterestedProductDto {
    private Long productId;
    private String productName;
    private String price;
    private String salesLink;
    private String imageLink;
    private List<String> element;
    private List<String> diagnoseType;
}
