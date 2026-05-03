package com.Nhom20.DoAnPhamMem.exception;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", errors);
        ApiResponse<Object> response = ApiResponse.builder().status(false).message("Validation failed: " + errors).data(null).build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Object> response = ApiResponse.builder().status(false).message(ex.getMessage()).build();
        return ResponseEntity.internalServerError().body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        // Log stacktrace để debug
        log.error("Unhandled exception occurred: ", ex);
        ApiResponse<Object> response = ApiResponse.builder().status(false).message("Hệ thống đang gặp sự cố, vui lòng thử lại sau.").build();
        return ResponseEntity.internalServerError().body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid Enum value: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.builder().status(false).message("Trạng thái không hợp lệ. Vui lòng kiểm tra lại dữ liệu gửi lên.").build();
        return ResponseEntity.badRequest().body(response);
    }
}
