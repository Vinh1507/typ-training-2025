package com.team7.StemHub.facade;

import com.team7.StemHub.dto.response.CourseResponse;
import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.view.SearchView;
import com.team7.StemHub.dto.response.UserResponse;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.service.CourseService;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchFacade {

    private final DocumentService documentService;
    private final UserService userService;
    private final CourseService courseService;

    public SearchView performSearch(String keyword, int userPage, int documentPage) {
        Set<CourseResponse> courses = courseService.searchCourses(keyword)
                .stream()
                .map(CourseResponse::new)
                .collect(Collectors.toSet());
        int documentPageIndex = (documentPage < 1) ? 0 : documentPage - 1;
        Page<Document> documentResultPage = documentService.searchDocuments(keyword, documentPageIndex);
        Page<DocumentResponse> documentResponsePage = documentResultPage.map(DocumentResponse::new);
        int userPageIndex = (userPage < 1) ? 0 : userPage - 1;
        Page<User> userResultPage = userService.searchUsers(keyword, userPageIndex);
        Page<UserResponse> userResponsePage = userResultPage.map(UserResponse::new);
        return SearchView.builder()
                .courses(courses)
                .documents(documentResponsePage)
                .users(userResponsePage)
                .keyword(keyword)
                .build();
    }
}