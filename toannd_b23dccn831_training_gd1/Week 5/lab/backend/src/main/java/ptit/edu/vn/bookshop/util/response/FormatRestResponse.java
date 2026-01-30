package ptit.edu.vn.bookshop.util.response;

import ptit.edu.vn.bookshop.domain.dto.response.ApiResponse;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = servletResponse.getStatus();
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatusCode(statusCode);

        if (body instanceof ApiResponse) {
            return body;
        }

        if (body instanceof String || body instanceof Resource) {
            return body;
        }

        if (statusCode >= 400) {
            res.setError("Call api failed");
            res.setMessage(body);
        } else {
            res.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(message != null ? message.value() : "Call api success");
        }
        return res;
    }
}
