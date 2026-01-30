package ptit.edu.vn.bookshop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", precision = 15, scale = 0, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "discount", precision = 15, scale = 0, nullable = false)
    private BigDecimal itemDiscount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}