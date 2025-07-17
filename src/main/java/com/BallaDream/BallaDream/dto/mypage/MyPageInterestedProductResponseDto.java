package com.BallaDream.BallaDream.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyPageInterestedProductResponseDto {
    private int totalPage;
    private int currentPage;
    List<MyPageInterestedProductDto> data;
}
