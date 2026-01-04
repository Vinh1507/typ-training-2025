// Đặt trong package (ví dụ): com.fintrack.backend.exceptions
package com.fintrack.backend.auth.exceptions;

import com.fintrack.backend.auth.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Bắt lỗi 'IllegalArgumentException' (lỗi "Email đã tồn tại")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo một đối tượng ErrorResponse đơn giản
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        // Gửi về cho frontend với lỗi 400 (Bad Request)
        // Frontend sẽ đọc được JSON { "message": "Email đã tồn tại" }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Bạn có thể thêm các @ExceptionHandler khác ở đây cho các lỗi khác
}