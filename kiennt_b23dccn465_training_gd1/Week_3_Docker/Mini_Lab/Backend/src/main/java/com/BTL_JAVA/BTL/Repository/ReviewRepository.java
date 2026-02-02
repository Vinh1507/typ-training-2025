package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // Kiểm tra xem review có tồn tại không
    boolean existsByUserId(int userId);
    
    // Query tối ưu với JOIN FETCH để tránh N+1 problem
    @Query("SELECT DISTINCT r FROM Review r " +
           "LEFT JOIN FETCH r.user " +
           "ORDER BY r.createdAt DESC")
    List<Review> findAllWithUser();
    
    // Query tối ưu cho một review
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user " +
           "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Integer reviewId);
    
    // Query tối ưu cho reviews của user
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user " +
           "WHERE r.user.id = :userId " +
           "ORDER BY r.createdAt DESC")
    List<Review> findByUserIdWithUser(@Param("userId") int userId);
    
    // Phân trang với user
    @Query(value = "SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.user",
           countQuery = "SELECT COUNT(r) FROM Review r")
    Page<Review> findAllWithUserPaginated(Pageable pageable);
    
    // Phân trang cho user cụ thể
    Page<Review> findByUserId(int userId, Pageable pageable);
    
    // Lấy reviews theo rating của USER (chỉ lấy reviews của users có rating)
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user u " +
           "WHERE u.rating = :rating " +
           "ORDER BY r.createdAt DESC")
    List<Review> findByRatingWithUser(@Param("rating") Integer rating);
    
    // Lấy reviews của users có rating >= giá trị cho trước
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user u " +
           "WHERE u.rating >= :minRating " +
           "ORDER BY r.createdAt DESC")
    List<Review> findByRatingGreaterThanEqualWithUser(@Param("minRating") Integer minRating);
    
    // Lấy tất cả reviews của users CÓ rating (không null)
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user u " +
           "WHERE u.rating IS NOT NULL " +
           "ORDER BY r.createdAt DESC")
    List<Review> findAllWithRating();
    
    // Lấy reviews của users KHÔNG có rating (null)
    @Query("SELECT r FROM Review r " +
           "LEFT JOIN FETCH r.user u " +
           "WHERE u.rating IS NULL " +
           "ORDER BY r.createdAt DESC")
    List<Review> findAllWithoutRating();
}
