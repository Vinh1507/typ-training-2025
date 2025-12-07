package com.oj.platform.core.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oj.platform.core.domain.pagination.PageResult;

import lombok.Getter;

/**
 * Framework-free API response structure for consistent envelopes.
 * <p>
 * success: boolean
 * message: string
 * data: generic payload
 * errors: map<string, string[]> — validation + summary errors
 * metadata: object (map) — includes pagination when applicable
 * statusCode: HTTP status code for the response
 */
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final Map<String, List<String>> errors; // all errors here
    private final Map<String, Object> metadata;
    // private final Integer statusCode; // HTTP status code

    public ApiResponse(
            boolean success,
            String message,
            T data,
            Map<String, List<String>> errors,
            Map<String, Object> metadata,
            HttpStatus statusCode) {
        this.success = success;
        this.message = message == null ? "" : message;
        this.data = data;
        this.errors = errors == null ? Collections.emptyMap() : errors;
        this.metadata = metadata == null ? Collections.emptyMap() : metadata;
        // this.statusCode = statusCode.getCode();
    }

    public boolean isSuccess() {
        return success;
    }

    // ===== SUCCESS FACTORIES =====

    /**
     * 200 OK - Generic success response
     *
     * @param data response data
     * @return ApiResponse with 200 OK
     */
    public static <T> ApiResponse<T> ok(T data) {
        return success(data, "Success");
    }

    /**
     * 200 OK - With custom message
     *
     * @param data    response data
     * @param message custom message
     * @return ApiResponse with 200 OK
     */
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, message, data, null, null, HttpStatus.OK);
    }

    /**
     * 201 Created - Resource successfully created
     *
     * @param data created resource
     * @return ApiResponse with 201 Created
     */
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, "Resource created successfully", data, null, null, HttpStatus.CREATED);
    }

    /**
     * 201 Created - With custom message
     *
     * @param data    created resource
     * @param message custom message
     * @return ApiResponse with 201 Created
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(true, message, data, null, null, HttpStatus.CREATED);
    }

    /**
     * 204 No Content - Success with no data
     *
     * @return ApiResponse with 204 No Content
     */
    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(true, "Operation successful", null, null, null, HttpStatus.NO_CONTENT);
    }

    /**
     * 204 No Content - With custom message
     *
     * @param message custom message
     * @return ApiResponse with 204 No Content
     */
    public static ApiResponse<Void> noContent(String message) {
        return new ApiResponse<>(true, message, null, null, null, HttpStatus.NO_CONTENT);
    }

    /**
     * Generic success response with custom status code
     *
     * @param data       response data
     * @param message    success message
     * @param statusCode HTTP status code
     * @return ApiResponse with custom status code
     */
    public static <T> ApiResponse<T> success(T data, String message, HttpStatus statusCode) {
        return new ApiResponse<>(true, message, data, null, null, statusCode != null ? statusCode : HttpStatus.OK);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, null, null, HttpStatus.OK);
    }

    /**
     * Generic success response with metadata
     *
     * @param data     response data
     * @param message  success message
     * @param metadata custom metadata
     * @return ApiResponse with 200 OK
     */
    public static <T> ApiResponse<T> ok(T data, String message, Map<String, Object> metadata) {
        return success(data, message, metadata, null);
    }

    /**
     * Generic success response with custom status and metadata
     *
     * @param data       response data
     * @param message    success message
     * @param statusCode HTTP status code
     * @param metadata   custom metadata
     * @return ApiResponse with custom status code
     */
    public static <T> ApiResponse<T> success(T data, String message,
            Map<String, Object> metadata, HttpStatus statusCode) {
        return new ApiResponse<>(true, message, data, null, metadata, statusCode);
    }

    // ===== NOT FOUND (404) =====

    /**
     * 404 Not Found - Resource not found
     *
     * @param message error message
     * @return ApiResponse with 404 Not Found
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, null, null, null, HttpStatus.NOT_FOUND);
    }

    /**
     * 404 Not Found - With metadata
     *
     * @param message  error message
     * @param metadata custom metadata
     * @return ApiResponse with 404 Not Found
     */
    public static <T> ApiResponse<T> notFound(String message, Map<String, Object> metadata) {
        return new ApiResponse<>(false, message, null, null, metadata, HttpStatus.NOT_FOUND);
    }

    // ===== BAD REQUEST (400) =====

    /**
     * 400 Bad Request - Simple error message
     *
     * @param message error message
     * @return ApiResponse with 400 Bad Request
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, Collections.emptyMap(), null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 400 Bad Request - Validation errors
     *
     * @param message error message
     * @param errors  field validation errors
     * @return ApiResponse with 400 Bad Request
     */
    public static <T> ApiResponse<T> error(String message, Map<String, List<String>> errors) {
        return new ApiResponse<>(false, message, null, errors, null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 400 Bad Request - With metadata
     *
     * @param message  error message
     * @param errors   field validation errors
     * @param metadata custom metadata
     * @return ApiResponse with 400 Bad Request
     */
    public static <T> ApiResponse<T> error(String message, Map<String, List<String>> errors,
            Map<String, Object> metadata) {
        return new ApiResponse<>(false, message, null, errors, metadata, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error response with custom HTTP status
     *
     * @param message    error message
     * @param errors     field errors
     * @param statusCode HTTP status code
     * @return ApiResponse with custom status code
     */
    public static <T> ApiResponse<T> error(String message, Map<String, List<String>> errors, HttpStatus statusCode) {
        return new ApiResponse<>(false, message, null, errors, null, statusCode);
    }

    /**
     * Error response with custom HTTP status and metadata
     *
     * @param message    error message
     * @param errors     field errors
     * @param metadata   custom metadata
     * @param statusCode HTTP status code
     * @return ApiResponse with custom status code
     */
    public static <T> ApiResponse<T> error(String message, Map<String, List<String>> errors,
            Map<String, Object> metadata, HttpStatus statusCode) {
        return new ApiResponse<>(false, message, null, errors, metadata, statusCode);
    }

    // ===== CONFLICT (409) =====

    /**
     * 409 Conflict - Resource already exists / duplicate
     *
     * @param message error message
     * @return ApiResponse with 409 Conflict
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, Collections.emptyMap(), HttpStatus.CONFLICT);
    }

    /**
     * 409 Conflict - With field errors
     *
     * @param message error message
     * @param errors  field errors
     * @return ApiResponse with 409 Conflict
     */
    public static <T> ApiResponse<T> conflict(String message, Map<String, List<String>> errors) {
        return error(message, errors, HttpStatus.CONFLICT);
    }

    // ===== UNAUTHORIZED (401) =====

    /**
     * 401 Unauthorized - Authentication required
     *
     * @param message error message
     * @return ApiResponse with 401 Unauthorized
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(message, Collections.emptyMap(), HttpStatus.UNAUTHORIZED);
    }

    // ===== FORBIDDEN (403) =====

    /**
     * 403 Forbidden - Access denied
     *
     * @param message error message
     * @return ApiResponse with 403 Forbidden
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(message, Collections.emptyMap(), HttpStatus.FORBIDDEN);
    }

    // ===== INTERNAL SERVER ERROR (500) =====

    /**
     * 500 Internal Server Error
     *
     * @param message error message
     * @return ApiResponse with 500 Internal Server Error
     */
    public static <T> ApiResponse<T> internalError(String message) {
        return error(message, Collections.emptyMap(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 500 Internal Server Error - With cause
     *
     * @param message error message
     * @param cause   error cause/exception
     * @return ApiResponse with 500 Internal Server Error
     */
    public static <T> ApiResponse<T> internalError(String message, Throwable cause) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("cause", cause != null ? cause.getMessage() : "Unknown error");
        return new ApiResponse<>(false, message, null, Collections.emptyMap(), metadata,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ===== PAGINATED RESPONSES =====

    /**
     * Paginated success response
     *
     * @param page    PageResult with items and pagination info
     * @param message success message
     * @return ApiResponse with 200 OK and pagination metadata
     */
    public static <U> ApiResponse<List<U>> fromPage(PageResult<U> page, String message) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("pagination", paginationMeta(page));
        return new ApiResponse<>(true, message == null ? "" : message, page.getItems(), null, meta, HttpStatus.OK);
    }

    /**
     * Paginated success response - Default message
     *
     * @param page PageResult with items and pagination info
     * @return ApiResponse with 200 OK and pagination metadata
     */
    public static <U> ApiResponse<List<U>> fromPage(PageResult<U> page) {
        return fromPage(page, "");
    }

    /**
     * Helper to shape pagination metadata consistently
     *
     * @param page PageResult object
     * @return pagination metadata map
     */
    public static Map<String, Object> paginationMeta(PageResult<?> page) {
        Map<String, Object> p = new HashMap<>();
        long total = page.getTotal();
        Integer perPageObj = page.getPerPage();
        Integer currentObj = page.getPage();
        int perPage = (perPageObj == null || perPageObj <= 0) ? (page.getItems() == null ? 0 : page.getItems().size())
                : perPageObj;
        int current = (currentObj == null || currentObj <= 0) ? 1 : currentObj; // treat as 1-based page number

        long totalPages = (perPage == 0) ? (total > 0 ? 1 : 0) : ((total + perPage - 1) / perPage);

        boolean hasPrev = current > 1;
        boolean hasNext = current < totalPages;

        p.put("page", current);
        p.put("perPage", perPage);
        p.put("total", total);
        p.put("totalPages", totalPages);
        p.put("count", page.getItems() == null ? 0 : page.getItems().size());
        p.put("hasPrev", hasPrev);
        p.put("hasNext", hasNext);
        p.put("prevPage", hasPrev ? current - 1 : null);
        p.put("nextPage", hasNext ? current + 1 : null);
        return p;
    }
}
