package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * DTO "bọc" (wrapper) chứa TẤT CẢ dữ liệu cần thiết
 * để render trang tài liệu yêu thích.
 * Nó TÁI SỬ DỤNG UserResponse và DocumentResponse.
 */
@Data
@Builder
public class UserFavoritesView {
    private UserResponse user;
    private Page<DocumentResponse> documents;
}