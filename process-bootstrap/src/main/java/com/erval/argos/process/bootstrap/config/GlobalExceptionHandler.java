package com.erval.argos.process.bootstrap.config;

import com.erval.argos.process.bootstrap.dto.StandardResultDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardResultDto<String>> handleIllegalArg(IllegalArgumentException ex) {
        StandardResultDto<String> resp = StandardResultDto.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardResultDto<String>> handleIllegalState(IllegalStateException ex) {
        StandardResultDto<String> resp = StandardResultDto.error(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResultDto<String>> handleGeneralException(Exception ex) {
        StandardResultDto<String> resp = StandardResultDto.error("An unexpected error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

}
