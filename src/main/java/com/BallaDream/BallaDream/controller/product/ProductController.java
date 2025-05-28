package com.BallaDream.BallaDream.controller.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.dto.product.RecommendationProductResponseDto;
import com.BallaDream.BallaDream.service.product.ProductService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    //피부 진단 직후, 화장품을 추천
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recommendation")
    public RecommendationProductResponseDto recommendProduct(@RequestParam DiagnoseType diagnoseType,
                                                             @RequestParam Level level,
                                                             @RequestParam int step,
                                                             @RequestParam(required = false) Integer minPrice,
                                                             @RequestParam(required = false) Integer maxPrice,
                                                             @RequestParam(required = false) String formulation) {

        return productService.createRecommend(userService.getUserId(), diagnoseType, level,formulation, minPrice, maxPrice, step);
    }
}
