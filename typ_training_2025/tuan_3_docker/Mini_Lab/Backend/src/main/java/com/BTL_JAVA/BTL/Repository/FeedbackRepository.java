package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    // Lấy tất cả feedback của một product với JOIN FETCH
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.user " +
           "LEFT JOIN FETCH f.product " +
           "WHERE f.product.productId = :productId " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByProductIdWithDetails(@Param("productId") Integer productId);

    // Lấy feedback theo ID với JOIN FETCH
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.user " +
           "LEFT JOIN FETCH f.product " +
           "WHERE f.id = :id")
    Optional<Feedback> findByIdWithDetails(@Param("id") Integer id);

    // Lấy tất cả feedback của user với JOIN FETCH
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.user " +
           "LEFT JOIN FETCH f.product " +
           "WHERE f.user.id = :userId " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByUserIdWithDetails(@Param("userId") Integer userId);

    // Kiểm tra user đã mua product chưa
    @Query("SELECT COUNT(od) > 0 FROM OrderDetail od " +
           "JOIN od.order o " +
           "WHERE o.user.id = :userId " +
           "AND od.productVariation.product.productId = :productId " +
           "AND o.status IN ('COMPLETED')")
    boolean hasUserPurchasedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    // Kiểm tra user đã feedback cho product chưa
    @Query("SELECT COUNT(f) > 0 FROM Feedback f " +
           "WHERE f.user.id = :userId " +
           "AND f.product.productId = :productId")
    boolean hasUserFeedbackedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    // Lấy feedback với phân trang theo product
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.user " +
           "LEFT JOIN FETCH f.product " +
           "WHERE f.product.productId = :productId")
    Page<Feedback> findByProductIdPaginated(@Param("productId") Integer productId, Pageable pageable);

    // Lấy feedback theo rating của product
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.user " +
           "LEFT JOIN FETCH f.product " +
           "WHERE f.product.productId = :productId " +
           "AND f.rating = :rating " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByProductIdAndRating(@Param("productId") Integer productId, @Param("rating") Integer rating);

    // Tính rating trung bình của product
    @Query("SELECT AVG(f.rating) FROM Feedback f " +
           "WHERE f.product.productId = :productId " +
           "AND f.rating IS NOT NULL")
    Double getAverageRatingByProductId(@Param("productId") Integer productId);

    // Đếm số lượng feedback theo rating của product
    @Query("SELECT COUNT(f) FROM Feedback f " +
           "WHERE f.product.productId = :productId " +
           "AND f.rating = :rating")
    Long countByProductIdAndRating(@Param("productId") Integer productId, @Param("rating") Integer rating);
}

