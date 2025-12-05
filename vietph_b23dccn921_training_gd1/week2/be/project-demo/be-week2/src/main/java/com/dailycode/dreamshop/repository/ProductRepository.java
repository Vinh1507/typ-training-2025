package com.dailycode.dreamshop.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dailycode.dreamshop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
  @Query("SELECT p FROM Product p WHERE p.price <= :price")
List<Product> findProductsByPrice( BigDecimal price);
}
