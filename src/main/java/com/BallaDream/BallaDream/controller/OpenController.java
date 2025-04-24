package com.BallaDream.BallaDream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenController {

    @GetMapping("/open")
    public String openApi() {
        return "ci cd success..";
    }
}
