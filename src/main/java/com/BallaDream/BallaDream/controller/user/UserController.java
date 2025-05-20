package com.BallaDream.BallaDream.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user")
    public String test() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("name: {}", name);
        return "login user!";
    }

    @GetMapping("/")
    public String test2() {
        return "balladream :) ~^!^~";
    }


}
