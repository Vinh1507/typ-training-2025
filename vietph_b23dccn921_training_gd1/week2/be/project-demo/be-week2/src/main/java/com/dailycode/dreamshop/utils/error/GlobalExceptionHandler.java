package com.dailycode.dreamshop.utils.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("statusCode", HttpStatus.BAD_REQUEST.value()); // 400
        body.put("suucess", false); // 400
        body.put("message", fieldErrors); // lỗi chi tiết từng field
        body.put("error_path", request.getRequestURI()); // vd: /auth/register

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({ NotFoundException.class, AlreadyExistException.class })
    public ResponseEntity<?> handleCustomErrors(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = ex instanceof NotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;

        Map<String, Object> body = new HashMap<>();
        body.put("statusCode", status.value());
        body.put("success", false);
        body.put("message", ex.getMessage());
        body.put("error_path", request.getRequestURI());

        return ResponseEntity.status(status).body(body);
    }
   
}
