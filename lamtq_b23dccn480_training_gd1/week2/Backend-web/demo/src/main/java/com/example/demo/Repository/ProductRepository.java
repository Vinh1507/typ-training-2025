package com.example.demo.Repository;

import com.example.demo.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByIsDeletedFalse();
    Optional<ProductEntity> findByIdAndIsDeletedFalse(long id);
    Optional<ProductEntity> findByProductNameAndIsDeletedFalse(String name);
    void deleteById(long id);
}
