package com.team7.StemHub.facade;

import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.view.UserFavoritesView;
import com.team7.StemHub.dto.view.UserProfileView;
import com.team7.StemHub.dto.response.UserResponse;
import com.team7.StemHub.exception.NotAuthenticatedException;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Facade điều phối (orchestrates) các quy trình nghiệp vụ liên quan đến User,
 * bao gồm trang profile và trang favorites.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final DocumentService documentService;

    /**
     * Lấy tất cả dữ liệu cần thiết cho trang profile của một user.
     * Toàn bộ logic (gọi service, check auth, DTO mapping) được chuyển từ controller.
     */
    public UserProfileView prepareUserProfileView(UUID userId, int page) {
        User user = userService.getUserByIdWithUploadFile(userId);
        UserResponse userResponse = new UserResponse(user);

        int pageIndex = (page < 1) ? 0 : page - 1;
        Page<Document> documents = documentService.getAllUploadDocumentsByAuthor(user, pageIndex);
        Page<DocumentResponse> documentPage = documents.map(DocumentResponse::new);

        // Logic nghiệp vụ: Lấy thông tin người dùng *hiện tại* đang xem trang
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = null;
        Set<UUID> likedIds = Set.of();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User currentUser = userService.findByUsernameWithAllData(userDetails.getUsername());
            if (currentUser != null) {
                currentUserId = currentUser.getId();
                if (currentUser.getFavoritesDocuments() != null) {
                    likedIds = currentUser.getFavoritesDocuments().stream()
                            .filter(Objects::nonNull)
                            .map(Document::getId)
                            .collect(Collectors.toSet());
                }
            }
        }

        // Trả về một DTO "bọc" duy nhất
        return UserProfileView.builder()
                .user(userResponse)
                .documents(documentPage)
                .currentUserId(currentUserId)
                .likedIds(likedIds)
                .build();
    }

    /**
     * Lấy tất cả dữ liệu cho trang "Tài liệu yêu thích" của người dùng *hiện tại*.
     * Ném ra NotAuthenticatedException nếu chưa đăng nhập.
     * Toàn bộ logic (auth, DTO mapping, Sắp xếp, Phân trang thủ công) được chuyển từ controller.
     */
    public UserFavoritesView prepareUserFavoritesView(int page) throws NotAuthenticatedException {
        // 1. Logic nghiệp vụ: Xác thực
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            currentUser = userService.findByUsernameWithAllData(username);
        }

        // Guard clause: Phải đăng nhập
        if (currentUser == null) {
            throw new NotAuthenticatedException("User must be logged in to view favorites.");
        }

        UserResponse userResponse = new UserResponse(currentUser);

        // 2. Logic nghiệp vụ: Lấy DTO, Sắp xếp, và Phân trang thủ công
        Set<Document> favoriteDocuments = currentUser.getFavoritesDocuments();

        // Tái sử dụng DTO của bạn, và sắp xếp in-memory
        List<DocumentResponse> favoriteDocumentsDTO = favoriteDocuments.stream()
                .map(DocumentResponse::new)
                .sorted(Comparator.comparing(DocumentResponse::getCreatedAt).reversed())
                .toList();

        // Logic phân trang thủ công
        int pageIndex = (page < 1) ? 0 : page - 1;
        Pageable pageRequest = PageRequest.of(pageIndex, 6); // Giả sử page size là 6
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), favoriteDocumentsDTO.size());

        List<DocumentResponse> pageContent;
        if (start > favoriteDocumentsDTO.size()) {
            pageContent = List.of();
        } else {
            pageContent = favoriteDocumentsDTO.subList(start, end);
        }

        Page<DocumentResponse> favoriteDocumentsPage = new PageImpl<>(
                pageContent,
                pageRequest,
                favoriteDocumentsDTO.size()
        );

        return UserFavoritesView.builder()
                .user(userResponse)
                .documents(favoriteDocumentsPage)
                .build();
    }
}