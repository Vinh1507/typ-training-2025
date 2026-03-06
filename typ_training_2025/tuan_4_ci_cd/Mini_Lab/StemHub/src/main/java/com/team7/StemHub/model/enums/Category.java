package com.team7.StemHub.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    NEWS ("Tin tức"),
    LESSON ("Bài giảng"),
    PROJECT ("Thực hành"),
    RESEARCH ("Nghiên cứu"),
    REFERENCE ("Tài liệu học tập"),
    OUTLINE ("Đề cương"),
    EXAM ("Đề thi/Đề kiểm tra"),
    OTHERS ("Khác");

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    Category(String displayName) {
        this.displayName = displayName;
    }

    public static List<Category> getAllDisplayNames() {
        return Arrays.stream(Category.values())
                .collect(Collectors.toList());
    }

}
