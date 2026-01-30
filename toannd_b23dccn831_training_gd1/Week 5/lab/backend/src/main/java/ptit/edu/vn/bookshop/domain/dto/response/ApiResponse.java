package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T>{
    private int statusCode;
    private String error;
    private Object message;
    private T data;
}
