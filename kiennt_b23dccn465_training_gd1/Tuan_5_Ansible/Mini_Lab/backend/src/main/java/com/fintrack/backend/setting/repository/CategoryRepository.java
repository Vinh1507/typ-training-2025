package com.fintrack.backend.setting.repository;

import com.fintrack.backend.setting.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser_Id(Long userId);
}
