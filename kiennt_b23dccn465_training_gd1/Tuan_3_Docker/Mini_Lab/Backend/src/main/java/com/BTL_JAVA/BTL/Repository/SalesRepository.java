package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Product.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales,Integer> {
    List<Sales> findByActive(Boolean active);
}
