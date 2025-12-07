package com.oj.platform.components.classmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String semester;
    private String academicYear;
    private Instant createdAt;
    private Instant updatedAt;
}
