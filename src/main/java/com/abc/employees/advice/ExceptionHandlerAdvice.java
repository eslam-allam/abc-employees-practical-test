package com.abc.employees.advice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.abc.employees.exception.EmployeeNotFoundException;
import com.abc.employees.model.ErrorDto;
import com.abc.employees.model.Pair;

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
        return ErrorDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ IllegalStateException.class, IOException.class })
    public ErrorDto handleException(IllegalStateException ex) {
        logger.catching(ex);
        return ErrorDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto handleException(IllegalArgumentException ex) {
        logger.catching(ex);
        return ErrorDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ErrorDto handleException(EmployeeNotFoundException ex) {
        logger.catching(ex);
        return ErrorDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleException(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> Pair.of(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        return ErrorDto.builder().message("Validation error occured").validationErrors(validationErrors).build();
    }

}
