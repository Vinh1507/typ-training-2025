package com.oj.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String semester;
    private String academicYear;
    private Instant createdAt;
    private Instant updatedAt;
}
