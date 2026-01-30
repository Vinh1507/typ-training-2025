package ptit.edu.vn.bookshop.domain.entity;

import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publishers")

public class Publisher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 200, unique = true)
    @Email(message = "Email is not valid")
    private String email;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;


    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books;


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
