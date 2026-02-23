package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.CourseResponse;
import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.Set;

@Data
@Builder
public class SearchView {
    private Set<CourseResponse> courses;
    private Page<DocumentResponse> documents;
    private Page<UserResponse> users;
    private String keyword;
}