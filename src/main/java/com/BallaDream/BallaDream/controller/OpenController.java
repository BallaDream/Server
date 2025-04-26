package com.BallaDream.BallaDream.controller;

import com.BallaDream.BallaDream.dto.product.RecommendationProductResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OpenController {

    @GetMapping("/open")
    public String openApi() {
        return "ci cd success..";
    }

    @PostMapping("/test")
    public String test(@RequestBody RecommendationProductResponseDto dto) {
        log.info("test: {}", dto.getDescription());
        log.info("test: {}", dto.getData().get(0).getProductName());
        log.info("test: {}", dto.getData().get(0).getElement().size());
        log.info("test: {}", dto.getData().get(1).getProductName());
        log.info("test: {}", dto.getData().get(1).getElement().size());
        return "test";
    }
}
