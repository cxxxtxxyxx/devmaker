package com.firstspringtoy.devmaker.exception;


import com.firstspringtoy.devmaker.dto.DevmakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.firstspringtoy.devmaker.exception.DevmakerErrorCode.INVALID_REQUEST;


@Slf4j
@RestControllerAdvice
public class DevmakerExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DevmakerException.class)
    public DevmakerErrorResponse handleException(DevmakerException e, HttpServletRequest request) {

        log.error("errorCode: {}, url: {}, message: {}", e.getDevmakerErrorcode(), request.getRequestURI(), e.getDetailMessage());

        return DevmakerErrorResponse
                .builder()
                .errorCode(e.getDevmakerErrorcode())
                .errorMessage(e.getDetailMessage())
                .build();
    }


    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DevmakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {

        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DevmakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public DevmakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {

        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DevmakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
