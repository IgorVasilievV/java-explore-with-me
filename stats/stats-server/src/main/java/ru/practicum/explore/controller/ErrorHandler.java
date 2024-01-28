package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.exception.ApiError;
import ru.practicum.explore.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handle(ValidationException e) {
        log.info("Exception! Incorrectly made request.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "Incorrectly made request.", HttpStatus.BAD_REQUEST.toString(),
                convertDateTime());
    }

    private String convertDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
