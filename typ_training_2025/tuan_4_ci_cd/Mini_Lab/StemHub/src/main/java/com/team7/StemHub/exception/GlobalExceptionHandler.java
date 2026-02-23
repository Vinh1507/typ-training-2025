package com.team7.StemHub.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.rmi.ServerException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaTypeException(HttpServletRequest request, MediaType mediaType) {
        String message = "Unsupported media type: " + mediaType;
        log.warn(message);

        ErrorResponse error = ErrorResponse.builder(new UnsupportedMediaTypeException(message), HttpStatus.UNSUPPORTED_MEDIA_TYPE, message)
                .title("Unsupported Media Type")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create(request.getRequestURI() + "/error"))
                .build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    // Upload file quá kích thước
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleMaxUpload(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("Upload file quá lớn: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.PAYLOAD_TOO_LARGE, "File size is too large")
                .title("Upload Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create(request.getRequestURI() + "/error"))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // Input không hợp lệ (validation @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        if (message == null) {
            message = "Invalid input: " + ex.getBindingResult().getAllErrors().get(0).getObjectName();
        }
        log.warn("Validation failed: {}", message);

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, message)
                .title("Validation Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create(request.getRequestURI() + "/error"))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // Not found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex,
                                                        HttpServletRequest request) {
        log.warn("Not found: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Not Found")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create(request.getRequestURI() + "/error"))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }



    // Lỗi client custom
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorResponse> handleRequestException(RequestException ex, HttpServletRequest request) {
        log.warn("Request Exception: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Request Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create(request.getRequestURI() + "/error"))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // Lỗi server custom
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServerException ex, HttpServletRequest request) {
        log.error("Server Exception: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage())
                .title("Server Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create("/not-found"))
                .build();

        return ResponseEntity.internalServerError().body(error);
    }

    // Lỗi còn lại (RuntimeException chung)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        log.error("Unhandled RuntimeException", ex);

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
                .title("Runtime Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create("/not-found"))
                .build();

        return ResponseEntity.internalServerError().body(error);
    }

    // Catch-all Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception", ex);

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error")
                .title("General Error")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create("/not-found"))
                .build();

        return ResponseEntity.internalServerError().body(error);
    }

    // Static resource not found (.well-known, devtools probes, etc.) – log ở mức DEBUG để tránh spam
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex, HttpServletRequest request) {
        // Nhiều trình duyệt / extension tự động gọi các path .well-known => không nên log ERROR
        log.debug("Static resource not found: {}", ex.getResourcePath());

        ErrorResponse error = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, "Resource not found")
                .title("Resource Not Found")
                .instance(URI.create(request.getRequestURI()))
                .type(URI.create("/not-found"))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}