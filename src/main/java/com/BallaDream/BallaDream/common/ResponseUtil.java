package com.BallaDream.BallaDream.common;

import com.BallaDream.BallaDream.dto.message.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void setErrorResponse(int status, HttpServletResponse res, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json; charset=UTF-8");

        ErrorDto messageDto = new ErrorDto(status, message);
        String jsonResponse = mapper.writeValueAsString(messageDto);

        res.getWriter().write(jsonResponse);
    }
}
