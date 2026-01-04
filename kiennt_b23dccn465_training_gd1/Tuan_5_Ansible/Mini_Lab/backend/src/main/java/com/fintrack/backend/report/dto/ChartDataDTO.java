package com.fintrack.backend.report.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ChartDataDTO {
    private Map<String, Double> categoryTotals;
    private Map<String, Double> monthlyExpenses;
    private Map<String, Double> monthlyIncomes;
    private Map<String, Double> expenseDistribution;
}