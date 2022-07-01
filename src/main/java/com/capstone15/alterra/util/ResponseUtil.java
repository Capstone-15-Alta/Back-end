package com.capstone15.alterra.util;

import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.common.ApiResponse2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {

    private ResponseUtil() {}

    public static <T> ResponseEntity<Object> build(String message, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build(message, data), httpStatus);
    }

    public static <T> ResponseEntity<Object> build(String message, String totalThread, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build2(message,totalThread, data), httpStatus);
    }

    private static <T> ApiResponse<T> build(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .data(data)
                .build();
    }

    private static <T> ApiResponse2<T> build2(String message, String totalThread, T data) {
        return ApiResponse2.<T>builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .totalThread(totalThread)
                .data(data)
                .build();
    }

}
