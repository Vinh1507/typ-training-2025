package ptit.edu.vn.bookshop.domain.entity;

import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email", length = 200, unique = true, nullable = false)
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "user",  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user",  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Address> addresses;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.status == null) {
            this.status = StatusEnum.ACTIVE;
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
