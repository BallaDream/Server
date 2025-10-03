package com.BallaDream.BallaDream.service.diagnose;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DateUtilTest {

    @Test
    void testDateForm() {

        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        String formattedDate = DateUtil.getFormattedDate(now);
        //then
        System.out.println(formattedDate);
    }

}