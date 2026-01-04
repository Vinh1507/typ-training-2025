package com.fintrack.backend.wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự sinh Getter, Setter, toString...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets") // Map đúng tên bảng
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_name") // Map cột 'wallet_name'
    private String walletName;

    @Column(name = "type")
    private String type;

    @Column(name = "balance")
    private Double balance = 0.0;

    @Column(name = "user_id") // Quan trọng: Map cột 'user_id'
    private Long userId;

    @Column(name = "bank_linked") // Map cột 'bank_linked'
    private String bankLinked;

    @Column(name = "account_number") // Map cột 'account_number'
    private String accountNumber;

    @Column(name = "account_name") // Map cột 'account_name'
    private String accountName;
}
