package com.fintrack.backend.report.controller;

import com.fintrack.backend.report.dto.ChartDataDTO;
import com.fintrack.backend.report.dto.FinancialReportDTO;
import com.fintrack.backend.report.dto.TrendDTO;
import com.fintrack.backend.report.service.AnalyticsService;
import com.fintrack.backend.auth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173"})
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/financial-report")
    public ResponseEntity<?> getFinancialReport(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "9") int month,
            @RequestParam(defaultValue = "2024") int year
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(service.getFinancialReport(user.getId(), month, year));
    }

    @GetMapping("/chart-data")
    public ResponseEntity<?> getChartData(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(service.getChartData(user.getId()));
    }

    @GetMapping("/trend")
    public ResponseEntity<?> getTrendAnalysis(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(service.getTrendAnalysis(user.getId()));
    }

    @GetMapping("/available-periods")
    public ResponseEntity<?> getAvailablePeriods(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(service.getAvailableReportPeriods(user.getId()));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<?> exportPdfReport(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        byte[] pdfBytes = service.exportFinancialReportPdf(user.getId(), month, year);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=financial-report-" + month + "-" + year + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<?> exportCsvReport(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        byte[] csvBytes = service.exportFinancialReportCsv(user.getId(), month, year);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=financial-report-" + month + "-" + year + ".csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvBytes);
    }
}