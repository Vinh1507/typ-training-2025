package com.dailycode.dreamshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycode.dreamshop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
  
    
}
