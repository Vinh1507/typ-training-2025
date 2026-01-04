package com.BTL_JAVA.BTL.Controller.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.CategoryCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.CategoryUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.CategoryResponse;
import com.BTL_JAVA.BTL.Service.Product.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @ModelAttribute @Valid CategoryCreationRequest req) throws IOException {
        return ResponseEntity.ok(categoryService.create(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Integer id,
                                                                @ModelAttribute CategoryUpdateRequest req) throws IOException {
        return ResponseEntity.ok(categoryService.update(id,req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> get(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(categoryService.list());
    }
}
