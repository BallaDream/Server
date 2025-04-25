package com.BallaDream.BallaDream.controller;

import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.dto.diagnose.TestDto;
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
    public String test(@RequestBody TestDto dto) {
        log.info("test: {}", dto.getData().size());
        return "test";
    }
}
