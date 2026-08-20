package com.blog_hub.exception;

import com.blog_hub.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//Before Standardize api responses
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(
//            ResourceNotFoundException ex,
//            HttpServletRequest request){
//        ErrorResponse response = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.NOT_FOUND.value())
//                .error("Not found")
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.<Void>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .data(null)
                                .build()
                );
    }

//    @ExceptionHandler(DuplicateResourceException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicate(
//            ResourceNotFoundException ex,
//            HttpServletRequest request){
//        ErrorResponse response = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.CONFLICT.value())
//                .error("Conflict")
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.<Void>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .data(null)
                                .build()
                );
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidation(
//            MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//
//        return ResponseEntity.badRequest().body(errors);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiResponse<Map<String, String>> response =
                ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(
//            Exception ex,
//            HttpServletRequest request) {
//
//        ErrorResponse response = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error("Internal Server Error")
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>>
    handleGeneralException(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.<Void>builder()
                                .success(false)
                                .message("An unexpected error occurred")
                                .data(null)
                                .build()
                );
    }
}
