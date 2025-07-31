package com.BallaDream.BallaDream.dto.mypage;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.Element;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyPageInterestedProductDto {
    private Long productId;
    private String productName;
    private String formulation;
    private int price;
    private String salesLink;
    private String imageLink;
    private List<String> element;
    private List<DiagnoseType> diagnoseType;
}
