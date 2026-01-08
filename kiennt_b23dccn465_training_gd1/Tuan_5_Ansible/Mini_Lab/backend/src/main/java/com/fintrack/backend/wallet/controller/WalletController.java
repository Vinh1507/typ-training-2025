package com.fintrack.backend.wallet.controller;

import com.fintrack.backend.wallet.model.Wallet;
import com.fintrack.backend.wallet.service.WalletService;
import com.fintrack.backend.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173", "http://192.168.123.83:81"})
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallets(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(walletService.getWalletsByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(
            @AuthenticationPrincipal User user,
            @RequestBody Wallet wallet
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        wallet.setUserId(user.getId()); // Tự động gán userId từ token
        return ResponseEntity.ok(walletService.createWallet(wallet));
    }

    @PutMapping("/{id}")
    public Wallet updateWallet(@PathVariable Long id, @RequestBody Wallet wallet) {
        return walletService.updateWallet(id, wallet);
    }

    @DeleteMapping("/{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
    }
}