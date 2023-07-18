package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorApi handleNotFoundException(final NotFoundException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .reason("Not found exception")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorApi handleValidateException(final ValidateException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .reason("Validation exception")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleConflictException(final ConflictException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .reason("Conflict exception")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleConflictException(final SQLException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .reason("SQL exception")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorApi handleConflictException(final MethodArgumentNotValidException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .reason("Method argument exception")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }
}
