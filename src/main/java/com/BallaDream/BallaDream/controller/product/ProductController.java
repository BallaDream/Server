package com.BallaDream.BallaDream.controller.product;

import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.dto.product.RecommendationProductResponseDto;
import com.BallaDream.BallaDream.service.product.ProductService;
import jakarta.websocket.server.PathParam;
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recommendation")
    public RecommendationProductResponseDto recommendProduct(@RequestParam DiagnosisType diagnosisType,
                                                             @RequestParam Level level,
                                                             @RequestParam int step,
                                                             @RequestParam(required = false) Integer minPrice,
                                                             @RequestParam(required = false) Integer maxPrice,
                                                             @RequestParam(required = false) String formulation) {

        return productService.createRecommend(diagnosisType, formulation, minPrice, maxPrice, step);
    }
}
