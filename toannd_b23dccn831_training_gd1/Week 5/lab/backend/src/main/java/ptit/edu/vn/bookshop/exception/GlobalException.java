package ptit.edu.vn.bookshop.exception;


import ptit.edu.vn.bookshop.domain.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("404 Not Found. URL may not exist...");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(res);
    }

    @ExceptionHandler({
            IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            DataIntegrityViolationException.class,
            IllegalStateException.class,
    })
    public ResponseEntity<ApiResponse<Object>> handleIdInvalidException(Exception e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Exception occurs...");
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(res);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileUploadException(Exception e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Exception upload file...");
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getBody().getDetail());

        List<String> erros = fieldErrors
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        res.setMessage(erros.size() > 1 ? erros : erros.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getConstraintViolations().iterator().next().getMessage());
        res.setMessage(e.getConstraintViolations().iterator().next().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ApiResponse<Object>> handlePermissionException(PermissionException ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setMessage(ex.getMessage());
        res.setError("You do not have permission to access this resource");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
