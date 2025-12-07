package com.oj.application.exception;

import com.oj.platform.core.http.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Global exception handler to ensure all errors return ApiResponse format
 */
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Environment env;

    public GlobalExceptionHandler(Environment env) {
        this.env = env;
    }

    private boolean isTraceEnabled() {
        boolean devtoolsPresent;
        try {
            Class.forName("org.springframework.boot.devtools.restart.Restarter");
            devtoolsPresent = true;
        } catch (ClassNotFoundException e) {
            devtoolsPresent = false;
        }
        boolean devtoolsEnabled = env.getProperty("spring.devtools.restart.enabled", Boolean.class, false);
        boolean appToggle = env.getProperty("app.error.include-trace", Boolean.class, false);
        boolean devProfile = env.acceptsProfiles(Profiles.of("local", "dev"));
        return devtoolsPresent || devtoolsEnabled || appToggle || devProfile;
    }

    private String stackTrace(Throwable ex) {
        StringWriter sw = new StringWriter(2048);
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private Map<String, Object> buildMetadata(String code, Throwable ex) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("code", code);
        meta.put("timestamp", OffsetDateTime.now());
        if (ex != null && isTraceEnabled()) {
            meta.put("trace", stackTrace(ex));
        }
        return meta;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Handle field errors
        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                          Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        errors.putAll(fieldErrors);

        // Handle global errors
        List<String> globalMessages = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        if (!globalMessages.isEmpty()) {
            errors.put("messages", globalMessages);
        }

        Map<String, Object> meta = buildMetadata("VALIDATION_ERROR", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error("Validation failed", errors, meta));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
        Map<String, Object> meta = buildMetadata("VALIDATION_ERROR", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error("Validation failed", errors, meta));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, List<String>> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getPropertyPath().toString(),
                        LinkedHashMap::new,
                        Collectors.mapping(v -> v.getMessage(), Collectors.toList())));
        Map<String, Object> meta = buildMetadata("VALIDATION_ERROR", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error("Validation failed", errors, meta));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put(ex.getName(), List.of("Invalid value"));
        Map<String, Object> meta = buildMetadata("BAD_REQUEST", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid request", errors, meta));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchElement(NoSuchElementException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("messages", List.of(ex.getMessage()));
        Map<String, Object> meta = buildMetadata("NOT_FOUND", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Resource not found", errors, meta));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred", ex);
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("messages", List.of(ex.getMessage() == null ? "An error occurred" : ex.getMessage()));
        Map<String, Object> meta = buildMetadata("RUNTIME_ERROR", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Operation failed", errors, meta));
    }

    @ExceptionHandler({ AuthorizationDeniedException.class, AccessDeniedException.class })
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(Exception ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("messages", List.of("Access Denied"));
        Map<String, Object> meta = buildMetadata("FORBIDDEN", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Access denied", errors, meta));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("Unexpected exception occurred", ex);
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("messages", List.of(ex.getMessage() == null ? "Internal Server Error" : ex.getMessage()));
        Map<String, Object> meta = buildMetadata("INTERNAL_ERROR", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal Server Error", errors, meta));
    }
}
