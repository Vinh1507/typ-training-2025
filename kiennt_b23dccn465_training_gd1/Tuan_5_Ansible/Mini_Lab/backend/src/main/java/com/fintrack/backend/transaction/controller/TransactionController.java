package com.fintrack.backend.transaction.controller;

import com.fintrack.backend.transaction.model.Transaction;
import com.fintrack.backend.transaction.service.TransactionService;
import com.fintrack.backend.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173"})
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // 1. Lấy danh sách (Trả về Page chuẩn của Spring)
    @GetMapping
    public ResponseEntity<Page<Transaction>> getAllTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction order
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        PageRequest pageable = PageRequest.of(page, limit, Sort.by(order, sortBy));
        Page<Transaction> result = transactionService.getTransactions(user.getId(), searchTerm, pageable);
        return ResponseEntity.ok(result);
    }

    // 2. Thêm mới
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody Transaction transaction
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        transaction.setUserId(user.getId()); // Tự động gán userId từ token

        Transaction newTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(201).body(newTransaction);
    }

    // 3. Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDetails));
    }

    // 4. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}