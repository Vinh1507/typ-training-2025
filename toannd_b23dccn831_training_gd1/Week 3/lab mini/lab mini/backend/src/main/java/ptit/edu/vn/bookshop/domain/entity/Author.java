package ptit.edu.vn.bookshop.domain.entity;

import jakarta.validation.constraints.NotBlank;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "authors")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "country")
    private String country;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String biography; // luu tieu su thong tin chi tiet tac giả

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


    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
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
