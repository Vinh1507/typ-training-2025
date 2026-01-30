package ptit.edu.vn.bookshop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    @Column(name = "comment", columnDefinition = "MEDIUMTEXT")
    private String comment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
