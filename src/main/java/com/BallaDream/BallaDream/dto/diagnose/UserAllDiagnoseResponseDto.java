package com.BallaDream.BallaDream.dto.diagnose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAllDiagnoseResponseDto {
    private int totalPage;
    private int currentPage;
    private List<UserAllDiagnoseDto> data = new ArrayList<>();
}
