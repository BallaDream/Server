package com.BallaDream.BallaDream.exception;

import com.BallaDream.BallaDream.dto.message.ErrorDto;
import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.exception.token.TokenException;
import com.BallaDream.BallaDream.exception.user.InvalidInputException;
import com.BallaDream.BallaDream.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *
     * @
     * @return
     */
    @ExceptionHandler(value = {UserException.class, TokenException.class})
    public ResponseEntity<ResponseDto> handleException(BaseCustomException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseDto.of(e.getHttpStatus(), e.getErrorMessage()));
    }

    // 입력값 검증이 유효하지 않을때 발생하는 예외를 잡아준다. ex) 사용자가 아이디를 이메일 형식으로 기입하지 않았음
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.of(HttpStatus.BAD_REQUEST, errorMsg));
    }

    //bin validation 으로 하기 힘든 사용자의 입력 데이터의 오류가 있는 경우 예외를 잡는다. ex) Map<Enum1, Enum2>
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> handleInvalidFormat(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.of(HttpStatus.BAD_REQUEST, "입력 양식이 잘못되었습니다"));
    }
}
