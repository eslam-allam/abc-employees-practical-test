package com.abc.employees.advice;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.abc.employees.exception.EmployeeNotFoundException;
import com.abc.employees.model.ErrorDto;

/**
 * ExceptionHandlerAdvice
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private final Logger logger = LogManager.getLogger();

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception ex) {
        logger.catching(ex);
        return new ErrorDto(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ IllegalStateException.class, IOException.class })
    public ErrorDto handleException(IllegalStateException ex) {
        logger.catching(ex);
        return new ErrorDto(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto handleException(IllegalArgumentException ex) {
        logger.catching(ex);
        return new ErrorDto(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ErrorDto handleException(EmployeeNotFoundException ex) {
        logger.catching(ex);
        return new ErrorDto(ex.getMessage());
    }

}
