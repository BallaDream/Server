package com.BallaDream.BallaDream.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String test() {
        return "login user!";
    }

    @GetMapping("/")
    public String test2() {
        return "balladream :) ~^!^~";
    }
}
