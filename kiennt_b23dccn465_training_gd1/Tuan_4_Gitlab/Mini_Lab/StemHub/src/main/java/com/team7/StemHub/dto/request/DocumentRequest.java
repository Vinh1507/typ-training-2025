package com.team7.StemHub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team7.StemHub.model.enums.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class DocumentRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("author_id")
    private UUID authorId;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("file")
    private MultipartFile file;

    @JsonProperty("thumbnail")
    private MultipartFile thumbnail;
}
