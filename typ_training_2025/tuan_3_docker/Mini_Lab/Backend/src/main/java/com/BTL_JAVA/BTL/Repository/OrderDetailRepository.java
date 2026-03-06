package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Orders.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    
    // Lấy order detail theo ID với JOIN FETCH
    @Query("SELECT od FROM OrderDetail od " +
           "LEFT JOIN FETCH od.order o " +
           "LEFT JOIN FETCH o.user " +
           "LEFT JOIN FETCH od.productVariation pv " +
           "LEFT JOIN FETCH pv.product " +
           "WHERE od.id = :id")
    Optional<OrderDetail> findByIdWithDetails(@Param("id") Integer id);
    
    // Lấy tất cả order details của một order
    @Query("SELECT od FROM OrderDetail od " +
           "LEFT JOIN FETCH od.productVariation pv " +
           "LEFT JOIN FETCH pv.product " +
           "WHERE od.order.id = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);
    
    // Lấy order details theo product
    @Query("SELECT od FROM OrderDetail od " +
           "LEFT JOIN FETCH od.order o " +
           "LEFT JOIN FETCH o.user " +
           "WHERE od.productVariation.product.productId = :productId")
    List<OrderDetail> findByProductId(@Param("productId") Integer productId);
}

