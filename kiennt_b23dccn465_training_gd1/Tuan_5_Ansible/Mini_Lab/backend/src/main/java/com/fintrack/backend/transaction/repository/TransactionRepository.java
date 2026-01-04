package com.fintrack.backend.transaction.repository;

import com.fintrack.backend.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND " +
            "(LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Transaction> searchTransactions(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.category = :category " +
            "AND t.isIncome = false " +
            "AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :month")
    BigDecimal calculateTotalSpent(@Param("userId") Long userId,
                                   @Param("category") String category,
                                   @Param("month") String month);
}