package com.fintrack.backend.wallet.service;

import com.fintrack.backend.wallet.model.Wallet;
import com.fintrack.backend.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    // 1. Lấy ví theo userId (CHỈ LẤY VÍ CỦA USER ĐANG ĐĂNG NHẬP)
    public List<Wallet> getWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    // 2. Tạo ví mới
    public Wallet createWallet(Wallet wallet) {
        if (wallet.getBalance() == null) wallet.setBalance(0.0);
        // userId đã được set từ Controller, không cần fallback
        return walletRepository.save(wallet);
    }

    // 3. Cập nhật ví (Sửa tên, số tài khoản...)
    public Wallet updateWallet(Long id, Wallet walletDetails) {
        Wallet existing = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        existing.setWalletName(walletDetails.getWalletName());
        existing.setType(walletDetails.getType());
        existing.setBalance(walletDetails.getBalance()); // Cho phép sửa số dư thủ công nếu cần
        existing.setBankLinked(walletDetails.getBankLinked());
        existing.setAccountNumber(walletDetails.getAccountNumber());
        existing.setAccountName(walletDetails.getAccountName());

        return walletRepository.save(existing);
    }

    // 4. Xóa ví
    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }

    // Hàm phụ: Cập nhật số dư (Dùng cho các Service khác gọi nếu cần)
    public void updateBalance(Long walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }
}