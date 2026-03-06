package com.BTL_JAVA.BTL.DTO.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

     int code=1000;
     String message;
     T result;

     public static <T> ApiResponse<T> ok(T data) {
          return ApiResponse.<T>builder()
                  .code(1000)
                  .message("Success")
                  .result(data)
                  .build();
     }

     public static <T> ApiResponse<T> ok(T data, String msg) {
          return ApiResponse.<T>builder()
                  .code(1000)
                  .message(msg)
                  .result(data)
                  .build();
     }

     public static <T> ApiResponse<T> error(int code, String msg) {
          return ApiResponse.<T>builder()
                  .code(code)
                  .message(msg)
                  .build();
     }
}
