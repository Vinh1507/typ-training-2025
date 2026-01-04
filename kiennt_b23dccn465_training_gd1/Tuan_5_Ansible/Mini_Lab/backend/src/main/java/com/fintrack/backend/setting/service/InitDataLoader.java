package com.fintrack.backend.setting.service;

import com.fintrack.backend.auth.models.User;
import com.fintrack.backend.auth.repositories.UserRepository;
import com.fintrack.backend.setting.model.Category;
import com.fintrack.backend.setting.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class InitDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(InitDataLoader.class);

    private final UserRepository userRepo;
    private final CategoryRepository catRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public InitDataLoader(UserRepository userRepo, CategoryRepository catRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.catRepo = catRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void load() {
        try {
            // Tạo user mẫu nếu chưa có
            if (userRepo.findByEmail("user@example.com").isEmpty()) {
                String hashed = passwordEncoder.encode("password");
                User user = new User();
                user.setEmail("user@example.com");
                user.setPassword(hashed);
                user.setFullName("User");
                user.setPhone("0123456789");
                userRepo.save(user);
                logger.info("Created sample user: user@example.com");
            }

            // Tạo categories mẫu nếu chưa có
            if (catRepo.count() == 0) {
                String[] categories = {
                        "Ăn uống", "Sinh hoạt", "Đi lại",
                        "Giải trí", "Giáo dục", "Y tế"
                };

                for (String categoryName : categories) {
                    Category category = new Category(categoryName);
                    catRepo.save(category);
                }
                logger.info("Created {} sample categories", categories.length);
            }

            logger.info("Database initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error during database initialization: {}", e.getMessage());
        }
    }
}