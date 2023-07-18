package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(error -> "Field: " + error.getField() + ". Error: " + error.getDefaultMessage()
                        + ". Value: " + error.getRejectedValue())
                .reduce((error1, error2) -> error1 + "; " + error2)
                .orElse("Validation failed");
        return allBadRequestErrorCodes(errorMsg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(WrongTimeException.class)
    public ResponseEntity handleIncorrectRequestException(WrongTimeException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity handleNumberFormatException(NumberFormatException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity handleDateTimeException(DateTimeException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity handleConversionFailedException(ConversionFailedException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        return allBadRequestErrorCodes(e.getMessage());
    }

    //400
    private ResponseEntity allBadRequestErrorCodes(String errorMsg) {
        HttpStatus code = HttpStatus.BAD_REQUEST;
        ErrorApi response = new ErrorApi(code, "bad request", errorMsg);
        log.warn(errorMsg);
        return ResponseEntity.status(code).body(response);
    }

    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorMsg = e.getMessage();
        HttpStatus code = HttpStatus.CONFLICT;
        ErrorApi response = new ErrorApi(code, "integrity violation", errorMsg);
        log.warn(errorMsg);
        return ResponseEntity.status(code).body(response);
    }

    // 500
    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleThrowable(Throwable e) {
        String errorMsg = e.getMessage();
        HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorApi response = new ErrorApi(code, "error", errorMsg);
        log.warn(errorMsg);
        return ResponseEntity.status(code).body(response);
    }
}
