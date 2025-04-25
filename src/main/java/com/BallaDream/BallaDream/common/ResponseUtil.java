package com.BallaDream.BallaDream.common;

import com.BallaDream.BallaDream.dto.message.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    // servlet response 에서 json 으로 하여 응답하게 해주는 함수
    public static void setErrorResponse(int status, HttpServletResponse res, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json; charset=UTF-8");

        ErrorDto messageDto = new ErrorDto(status, message);
        String jsonResponse = mapper.writeValueAsString(messageDto);

        res.getWriter().write(jsonResponse);
    }

    //binding result 에서 에러 메시지를 추출하여 반환하는 함수
    public static String getErrorMessages(List<ObjectError> errors) {
        return errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
    }
}
