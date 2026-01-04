package com.BTL_JAVA.BTL.Exception;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception ex){
       ApiResponse response = new ApiResponse<>();

       response.setMessage(ErrorCode.UNCATEGORIED_EXCEPTION.getMessage());
       response.setCode(ErrorCode.UNCATEGORIED_EXCEPTION.getCode());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppExcrptin(AppException ex){
        ErrorCode errorCode=ex.getErrorCode();
//        ApiResponse response = new ApiResponse<>();
//        response.setMessage(errorCode.getMessage());
//        response.setCode(errorCode.getCode());
//
//        return ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(response);\
        // Ưu tiên dùng message cụ thể từ exception; nếu rỗng thì dùng message mặc định của ErrorCode
        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : errorCode.getMessage();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(message)
                        .build());
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex){
        ErrorCode errorCode=ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode
                        .getStatusCode())
                        .body(ApiResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build());

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException ex){
        String enumKey=ex.getFieldError().getDefaultMessage();
         ErrorCode errorCode=ErrorCode.INVALID_KEY;
         Map<String,Object> attributes=null;

         try {
             errorCode = ErrorCode.valueOf(enumKey);
             var constrainViolation=ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

             attributes =constrainViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
         }catch (IllegalArgumentException e){

         }
        ApiResponse response = new ApiResponse<>();
        response.setMessage(Objects.nonNull(attributes)?
                mapAttribute(errorCode.getMessage(),attributes)
                : errorCode.getMessage());
        response.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(response);
    }

    private String mapAttribute(String message, Map<String,Object> atttributes){
         String minValue=String.valueOf(atttributes.get(MIN_ATTRIBUTE));

        return message.replace("{"+MIN_ATTRIBUTE+"}",minValue);
    }
}
