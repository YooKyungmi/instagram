package com.example.demo.src;

import com.example.demo.config.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static com.example.demo.config.BaseResponseStatus.WRONG_REQUEST;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public BaseResponse<String> constraintViolationException( ){
        return new BaseResponse<>(WRONG_REQUEST);
    }
}
