package com.BallaDream.BallaDream.exception;

import com.BallaDream.BallaDream.dto.message.ErrorDto;
import com.BallaDream.BallaDream.exception.user.InvalidInputException;
import com.BallaDream.BallaDream.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 사용자 관련 예외 처리
     * @param e
     * @return errorDto
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDto> handleUserException(UserException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorDto.of(e.getStatus(), e.getErrorMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDto> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.of(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.info("Validation 실패: {}", errorMsg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.of(HttpStatus.BAD_REQUEST, errorMsg));
    }
}
