package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.user.JoinDto;
import com.BallaDream.BallaDream.service.JoinService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    @PostMapping("/join")
    public Object joinProcess(@RequestBody @Valid JoinDto joinDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        joinService.joinProcess(joinDto);
        return "ok";
    }
}
