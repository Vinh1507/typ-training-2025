package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Integer>, JpaSpecificationExecutor<ProductVariation> {
    boolean existsByProduct_ProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
            Integer productId, String size, String color);

    boolean existsByProduct_ProductIdAndSizeIgnoreCaseAndColorIgnoreCaseAndIdNot(
            Integer productId, String size, String color, Integer excludeId);
}
