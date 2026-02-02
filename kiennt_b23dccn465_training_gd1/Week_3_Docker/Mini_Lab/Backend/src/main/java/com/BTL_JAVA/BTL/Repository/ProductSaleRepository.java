package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Integer> {
    List<ProductSale> findBySaleId(Integer saleId);
    
    /**
     * Tìm ProductSale đang active cho một sản phẩm cụ thể
     */
    @Query("SELECT ps FROM ProductSale ps " +
           "JOIN FETCH ps.sale s " +
           "WHERE ps.product.productId = :productId " +
           "AND s.stDate <= :now " +
           "AND s.endDate >= :now " +
           "ORDER BY ps.saleValue DESC")
    List<ProductSale> findActiveProductSaleByProductId(
        @Param("productId") Integer productId, 
        @Param("now") LocalDateTime now
    );
    List<ProductSale> findByProduct(Product product);

}