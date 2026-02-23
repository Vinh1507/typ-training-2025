package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.response.UserResponse;
import com.team7.StemHub.model.enums.Category;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class HomePageView {
    private Page<DocumentResponse> newestDocumentsPage;
    private List<DocumentResponse> topDocuments;
    private List<UserResponse> topUsers;
    private List<Category> categories;
    private String category;
}