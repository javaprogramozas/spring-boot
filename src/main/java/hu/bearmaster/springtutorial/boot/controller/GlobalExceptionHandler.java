package hu.bearmaster.springtutorial.boot.controller;

import hu.bearmaster.springtutorial.boot.model.exception.NotFoundException;
import hu.bearmaster.springtutorial.boot.model.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            sb.append(fieldError.getField())
                    .append(" -> ")
                    .append(fieldError.getDefaultMessage())
                    .append("\n");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(sb.toString()));
    }
}
