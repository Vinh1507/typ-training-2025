package com.fintrack.backend.report.service;

import com.fintrack.backend.report.dto.ChartDataDTO;
import com.fintrack.backend.report.dto.FinancialReportDTO;
import com.fintrack.backend.report.dto.TrendDTO;
import com.fintrack.backend.report.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository repo;
    private final ExportService exportService;

    @Override
    public FinancialReportDTO getFinancialReport(Long userId, int month, int year) {
        log.info("Getting financial report for user {} for: {}/{}", userId, month, year);

        // Lấy dữ liệu thật từ database với userId filter
        Double totalIncome = repo.getTotalIncomeByMonth(userId, month, year);
        Double totalExpense = repo.getTotalExpenseByMonth(userId, month, year);

        // Xử lý null values
        totalIncome = totalIncome != null ? totalIncome : 0.0;
        totalExpense = totalExpense != null ? totalExpense : 0.0;
        double balance = totalIncome - totalExpense;

        // Lấy chi tiết theo danh mục
        Map<String, Double> categorySummary = new HashMap<>();
        List<Object[]> categoryResults = repo.getCategoryExpensesByMonth(userId, month, year);

        for (Object[] result : categoryResults) {
            String category = (String) result[0];
            Double amount = (Double) result[1];
            if (category != null && amount != null) {
                categorySummary.put(category, amount);
            }
        }

        FinancialReportDTO dto = new FinancialReportDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setBalance(balance);
        dto.setCategorySummary(categorySummary);
        dto.setMonth(month);
        dto.setYear(year);

        return dto;
    }

    @Override
    public ChartDataDTO getChartData(Long userId) {
        log.info("Getting chart data for user {} from real database", userId);

        ChartDataDTO dto = new ChartDataDTO();

        // Lấy dữ liệu biểu đồ tròn từ tháng hiện tại
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);

        Map<String, Double> categoryTotals = new HashMap<>();
        List<Object[]> categoryResults = repo.getCategoryExpensesByMonth(userId, currentMonth, currentYear);

        for (Object[] result : categoryResults) {
            String category = (String) result[0];
            Double amount = (Double) result[1];
            if (category != null && amount != null) {
                categoryTotals.put(category, amount);
            }
        }
        dto.setCategoryTotals(categoryTotals);

        // Lấy dữ liệu xu hướng 12 tháng gần nhất
        Map<String, Double> monthlyExpenses = new LinkedHashMap<>();
        Map<String, Double> monthlyIncomes = new LinkedHashMap<>();

        List<Object[]> trendResults = repo.getMonthlyTrend(userId);
        for (Object[] result : trendResults) {
            String period = (String) result[0];
            Double income = (Double) result[1];
            Double expense = (Double) result[2];

            monthlyIncomes.put(period, income != null ? income : 0.0);
            monthlyExpenses.put(period, expense != null ? expense : 0.0);
        }

        dto.setMonthlyExpenses(monthlyExpenses);
        dto.setMonthlyIncomes(monthlyIncomes);

        return dto;
    }

    @Override
    public List<TrendDTO> getTrendAnalysis(Long userId) {
        log.info("Getting trend analysis for user {} from real database", userId);

        List<TrendDTO> trends = new ArrayList<>();
        List<Object[]> trendResults = repo.getMonthlyTrend(userId);

        for (Object[] result : trendResults) {
            String period = (String) result[0];
            Double income = (Double) result[1];
            Double expense = (Double) result[2];

            income = income != null ? income : 0.0;
            expense = expense != null ? expense : 0.0;
            double balance = income - expense;

            TrendDTO trend = new TrendDTO();
            trend.setPeriod(period);
            trend.setIncome(income);
            trend.setExpense(expense);
            trend.setBalance(balance);
            trends.add(trend);
        }

        return trends;
    }

    @Override
    public byte[] exportFinancialReportPdf(Long userId, int month, int year) {
        log.info("Exporting PDF report for user {} for: {}/{}", userId, month, year);
        FinancialReportDTO report = getFinancialReport(userId, month, year);
        return exportService.exportToPdf(report);
    }

    @Override
    public byte[] exportFinancialReportCsv(Long userId, int month, int year) {
        log.info("Exporting CSV report for user {} for: {}/{}", userId, month, year);
        FinancialReportDTO report = getFinancialReport(userId, month, year);
        return exportService.exportToCsv(report);
    }

    @Override
    public List<String> getAvailableReportPeriods(Long userId) {
        List<String> periods = new ArrayList<>();
        List<Integer> years = repo.findAvailableYears(userId);

        for (Integer year : years) {
            List<Integer> months = repo.findAvailableMonthsByYear(userId, year);
            for (Integer month : months) {
                periods.add(String.format("%d-%02d", year, month));
            }
        }

        return periods.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}