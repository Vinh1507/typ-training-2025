package com.team7.StemHub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team7.StemHub.model.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "document", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title"}),
})
public class Document {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "filePath", nullable = false)
    private String filePath;

    @Column(name = "thumbnailPath")
    private String thumbnailPath;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "downloadCount", nullable = false)
    @ColumnDefault("0")
    private int downloadCount = 0;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany(mappedBy = "favoritesDocuments")
    private Set<User> favoredByUsers;
}