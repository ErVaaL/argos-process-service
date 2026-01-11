package com.erval.argos.process.bootstrap.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardResultDto<T> {

    private String message;
    private T data;
    private String error;
    private Integer statusCode;
    private LocalDateTime timestamp;

    public static <T> StandardResultDto<T> success(String message, T data) {
        return StandardResultDto.<T>builder()
                .message(message)
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> StandardResultDto<T> error(String errorMessage, int statusCode) {
        return StandardResultDto.<T>builder()
                .error(errorMessage)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
