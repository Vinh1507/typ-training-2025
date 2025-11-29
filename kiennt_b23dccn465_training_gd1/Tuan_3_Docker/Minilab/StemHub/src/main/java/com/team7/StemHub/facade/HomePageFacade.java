package com.team7.StemHub.facade;

import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.view.HomePageView;
import com.team7.StemHub.dto.response.UserResponse;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Category;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomePageFacade {
    private final DocumentService documentService;
    private final UserService userService;

    public HomePageView prepareHomePageData(int page) {
        if (page < 1) page = 1;
        Page<Document> newestDocumentsPage = documentService.getNewestDocuments(page - 1);
        int totalPages = newestDocumentsPage.getTotalPages();
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
            newestDocumentsPage = documentService.getNewestDocuments(page - 1);
        }
        Page<DocumentResponse> responsePage = newestDocumentsPage.map(DocumentResponse::new);
        var topDocuments = documentService.getTopDocument();
        List<DocumentResponse> topDocumentsDTO = topDocuments.stream().map(DocumentResponse::new).toList();
        List<User> topUsers = userService.getTop10UsersOrderByDocument();
        List<UserResponse> topUsersDTO = topUsers.stream().map(UserResponse::new).toList(); // Tái sử dụng UserResponse
        List<Category> categories = Category.getAllDisplayNames();
        return HomePageView.builder()
                .newestDocumentsPage(responsePage)
                .topDocuments(topDocumentsDTO)
                .topUsers(topUsersDTO)
                .categories(categories)
                .category(null)
                .build();
    }

    public HomePageView prepareCategoryPageData(String category, int page) {
        List<DocumentResponse> topDocuments = documentService.getDocumentsByCategorySortedByDownloadCount(category)
                .stream().map(DocumentResponse::new).toList();
        int pageIndex = (page < 1) ? 0 : page - 1;
        Page<Document> documentPage = documentService.getDocumentsByCategorySortedByCreatedAt(category, pageIndex);
        Page<DocumentResponse> newestDocumentsPage = documentPage.map(DocumentResponse::new);
        List<UserResponse> topUsers = userService.getTop10UsersOrderByDocument()
                .stream().map(UserResponse::new).toList();
        List<Category> categories = Category.getAllDisplayNames();
        return HomePageView.builder()
                .newestDocumentsPage(newestDocumentsPage)
                .topDocuments(topDocuments)
                .topUsers(topUsers)
                .categories(categories)
                .category(category)
                .build();
    }
}