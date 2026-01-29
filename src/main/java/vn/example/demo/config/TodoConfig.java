package vn.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.example.demo.TodoValidator;

@Configuration
public class TodoConfig {
    /**
     * Tạo ra Bean TodoValidator để sử dụng sau này
     * 
     * @return
     */
    @Bean
    public TodoValidator validator() {
        return new TodoValidator();
    }
}
