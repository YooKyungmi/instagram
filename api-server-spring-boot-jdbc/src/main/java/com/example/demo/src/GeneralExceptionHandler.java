package com.example.demo.src;

import com.example.demo.config.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static com.example.demo.config.BaseResponseStatus.NOT_SUPPORTED;
import static com.example.demo.config.BaseResponseStatus.WRONG_REQUEST;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public BaseResponse<String> constraintViolationException( ){
        return new BaseResponse<>(WRONG_REQUEST);
    }


    @ExceptionHandler({OAuth2AuthenticationException.class})
    public BaseResponse<String> OAuth2AuthenticationException( ){
        return new BaseResponse<>(NOT_SUPPORTED);
    }

}
