package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.CourseResponse;
import com.team7.StemHub.model.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UploadFormView {
    private List<CourseResponse> courses;
    private Category[] categories;
    private UUID userId;
}