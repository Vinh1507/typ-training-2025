package ptit.edu.vn.bookshop.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "price", precision = 15, scale = 0, nullable = false)
    private BigDecimal price;

    @Column(name = "quantity",  nullable = false)
    private Integer quantity;

    @Column(name = "discount", precision = 15, scale = 0, nullable = false)
    private BigDecimal discount;

    @Column(name = "image")
    private String image;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;


    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartDetails;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id",  nullable = false)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",  nullable = false)
    private Category category;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        if (this.status == null) {
            if (this.quantity != null && this.quantity > 0) {
                this.status = BookStatusEnum.AVAILABLE;
            } else {
                this.status = BookStatusEnum.OUT_OF_STOCK;
            }
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
