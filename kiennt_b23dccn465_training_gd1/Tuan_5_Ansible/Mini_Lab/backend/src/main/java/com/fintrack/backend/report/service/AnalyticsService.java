package com.fintrack.backend.report.service;

import com.fintrack.backend.report.dto.ChartDataDTO;
import com.fintrack.backend.report.dto.FinancialReportDTO;
import com.fintrack.backend.report.dto.TrendDTO;
import java.util.List;

public interface AnalyticsService {
    FinancialReportDTO getFinancialReport(Long userId, int month, int year);
    ChartDataDTO getChartData(Long userId);
    List<TrendDTO> getTrendAnalysis(Long userId);
    List<String> getAvailableReportPeriods(Long userId);

    byte[] exportFinancialReportPdf(Long userId, int month, int year);
    byte[] exportFinancialReportCsv(Long userId, int month, int year);
}