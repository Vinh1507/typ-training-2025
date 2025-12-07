package com.oj.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassRequest {
    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Class code is required")
    private String code;

    private String description;
    private String semester;
    private String academicYear;
}
