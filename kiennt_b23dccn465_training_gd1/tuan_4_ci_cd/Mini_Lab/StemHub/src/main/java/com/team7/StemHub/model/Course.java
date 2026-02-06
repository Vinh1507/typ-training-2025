package com.team7.StemHub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"courseId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Course", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"courseName"})
})
public class Course {
    @Id
    @Column(name = "courseId", nullable = false)
    private String courseId;

    @Column(name = "courseName", nullable = false, length = 200)
    private String courseName;

    @Column(name = "otherName", length = 1000)
    private String otherName;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> docs;
}