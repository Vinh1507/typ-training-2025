package vn.example.demo;

import java.util.Optional;
import org.thymeleaf.util.StringUtils;

import vn.example.demo.domain.Todo;

public class TodoValidator {
    /**
     * Kiểm tra một object Todo có hợp lệ không
     * 
     * @param todo
     * @return
     */
    public boolean isValid(Todo todo) {
        return Optional.ofNullable(todo)
                .filter(t -> !StringUtils.isEmpty(t.getTitle())) // Kiểm tra title khác rỗng
                .filter(t -> !StringUtils.isEmpty(t.getDetail())) // Kiểm tra detail khác rỗng
                .isPresent(); // Trả về true nếu hợp lệ, ngược lại false
    }
}
