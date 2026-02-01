package com.oj_cpp.submission.infra.persistence.jpa.entity;

import com.oj_cpp.submission.domain.enums.JudgeResultEnum;
import com.oj_cpp.submission.domain.enums.LanguageEnum;
import com.oj_cpp.submission.domain.enums.SubmissionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LanguageEnum language;

    @Enumerated(EnumType.STRING)
    @Column(name = "judge_result", length = 20)
    private JudgeResultEnum judgeResult;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SubmissionStatusEnum status;

    @Column(name = "execution_time")
    private Integer executionTime;

    @Column(name = "memory_used")
    private Integer memoryUsed;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = Instant.now();
        if (status == null) {
            status = SubmissionStatusEnum.PENDING;
        }
    }
}
