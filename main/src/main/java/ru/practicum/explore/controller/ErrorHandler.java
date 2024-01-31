package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.model.exception.ApiError;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.RequestConflictException;
import ru.practicum.explore.model.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handle(MethodArgumentNotValidException e) {
        log.info("Exception! Incorrectly made request.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "Incorrectly made request.", HttpStatus.BAD_REQUEST.toString(), convertDateTime());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handle(NumberFormatException e) {
        log.info("Exception! Incorrectly made request.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "Incorrectly made request.", HttpStatus.BAD_REQUEST.toString(),
                convertDateTime());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handle(ValidationException e) {
        log.info("Exception! Incorrectly made request.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "Incorrectly made request.", HttpStatus.BAD_REQUEST.toString(),
                convertDateTime());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiError handle(ConstraintViolationException e) {
        log.info("Integrity constraint has been violated.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.toString(), convertDateTime());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiError handle(RequestConflictException e) {
        log.info("For the requested operation the conditions are not met.");
        e.printStackTrace();
        return new ApiError(e.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.toString(), convertDateTime());
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(NotFoundException e) {
        log.info(("Exception! Not found entity"));
        return new ApiError(e.getMessage(), "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(), convertDateTime());
    }


    private String convertDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
