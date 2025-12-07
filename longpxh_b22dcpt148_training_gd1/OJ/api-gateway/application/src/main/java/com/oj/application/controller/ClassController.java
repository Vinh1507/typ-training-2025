package com.oj.application.controller;

import com.oj.application.dto.request.CreateClassRequest;
import com.oj.application.dto.response.ClassResponse;
import com.oj.platform.components.classmanagement.domain.model.ClassEntity;
import com.oj.platform.components.classmanagement.domain.service.ClassService;
import com.oj.platform.core.domain.pagination.PageResult;
import com.oj.platform.core.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassResponse> create(@Valid @RequestBody CreateClassRequest request) {
        ClassEntity classEntity = ClassEntity.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .semester(request.getSemester())
                .academicYear(request.getAcademicYear())
                .build();

        ClassEntity created = classService.createClass(classEntity);
        ClassResponse response = toResponse(created);

        return ApiResponse.success(response, "Class created successfully");
    }

    @GetMapping
    public ApiResponse<List<ClassResponse>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ClassEntity> classes = classService.findAll(pageable);
        
        PageResult<ClassResponse> result = PageResult.of(
                classes.getContent().stream().map(this::toResponse).toList(),
                (int) classes.getTotalElements(),
                page,
                size
        );

        return ApiResponse.fromPage(result, "Classes retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassResponse> getById(@PathVariable Long id) {
        ClassEntity classEntity = classService.findById(id);
        ClassResponse response = toResponse(classEntity);
        return ApiResponse.success(response, "Class retrieved successfully");
    }

    @PostMapping("/{classId}/assign-teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> assignTeacher(
            @PathVariable Long classId,
            @PathVariable Long teacherId) {
        classService.assignTeacherToClass(teacherId, classId);
        return ApiResponse.success(null, "Teacher assigned to class successfully");
    }

    @PostMapping("/{classId}/assign-student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> assignStudent(
            @PathVariable Long classId,
            @PathVariable Long studentId) {
        classService.assignStudentToClass(studentId, classId);
        return ApiResponse.success(null, "Student assigned to class successfully");
    }

    private ClassResponse toResponse(ClassEntity classEntity) {
        return ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .code(classEntity.getCode())
                .description(classEntity.getDescription())
                .semester(classEntity.getSemester())
                .academicYear(classEntity.getAcademicYear())
                .createdAt(classEntity.getCreatedAt())
                .updatedAt(classEntity.getUpdatedAt())
                .build();
    }
}
