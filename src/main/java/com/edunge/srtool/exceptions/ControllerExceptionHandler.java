package com.edunge.srtool.exceptions;

import com.edunge.srtool.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse exceptionHandler(Exception ex){
        LOGGER.error(ex.getLocalizedMessage());
        System.out.println("Ameh: "+ex.getMessage());
        System.out.println("Ameh 2: "+ex);
        return new ErrorResponse("99", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse invalidCredentialsException(InvalidCredentialsException ex){
        return new ErrorResponse(ex.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse invalidCredentialsException(NotFoundException ex){
        return new ErrorResponse(ex.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse duplicateRequest(DuplicateException ex){
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }
}
