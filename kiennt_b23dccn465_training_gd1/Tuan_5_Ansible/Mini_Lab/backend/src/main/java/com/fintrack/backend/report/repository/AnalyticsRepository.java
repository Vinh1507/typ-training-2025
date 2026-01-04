package com.fintrack.backend.report.repository;

import com.fintrack.backend.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findByMonthYear(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
            "WHERE t.userId = :userId AND t.isIncome = false AND MONTH(t.date) = :month AND YEAR(t.date) = :year " +
            "GROUP BY t.category")
    List<Object[]> getCategoryExpensesByMonth(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    // Sửa query này thành native SQL và thêm userId filter
    @Query(value = "SELECT DATE_FORMAT(t.date, '%Y-%m') as period, " +
            "SUM(CASE WHEN t.is_income = true THEN t.amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN t.is_income = false THEN t.amount ELSE 0 END) as expense " +
            "FROM transactions t " +
            "WHERE t.user_id = :userId AND t.date >= DATE_SUB(CURRENT_DATE, INTERVAL 12 MONTH) " +
            "GROUP BY DATE_FORMAT(t.date, '%Y-%m') " +
            "ORDER BY period", nativeQuery = true)
    List<Object[]> getMonthlyTrend(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userId = :userId AND t.isIncome = true AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    Double getTotalIncomeByMonth(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userId = :userId AND t.isIncome = false AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    Double getTotalExpenseByMonth(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT DISTINCT YEAR(t.date) as year FROM Transaction t WHERE t.userId = :userId ORDER BY year DESC")
    List<Integer> findAvailableYears(@Param("userId") Long userId);

    @Query("SELECT DISTINCT MONTH(t.date) as month FROM Transaction t WHERE t.userId = :userId AND YEAR(t.date) = :year ORDER BY month")
    List<Integer> findAvailableMonthsByYear(@Param("userId") Long userId, @Param("year") int year);
}