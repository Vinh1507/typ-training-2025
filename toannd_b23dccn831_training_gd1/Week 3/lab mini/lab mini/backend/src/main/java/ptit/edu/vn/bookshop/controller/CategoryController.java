package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.CategoryRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CategoryResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.CategoryPageResponseDTO;
import ptit.edu.vn.bookshop.service.CategoryService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Category created successfully")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO categoryResponseDTO = this.categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @PutMapping("/categories/{id}")
    @ApiMessage("Category updated successfully")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable Long id){
        CategoryResponseDTO categoryResponseDTO = this.categoryService.updateCategory(categoryRequestDTO, id);
        return ResponseEntity.ok().body(categoryResponseDTO);
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Category deleted successfully")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        this.categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Category retrieved successfully")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id){
        CategoryResponseDTO categoryResponseDTO = this.categoryService.fetchCategory(id, false);
        return ResponseEntity.ok().body(categoryResponseDTO);
    }

    @GetMapping("/categories")
    @ApiMessage("Categories retrieved successfully")
    public ResponseEntity<CategoryPageResponseDTO> getAllCategories(Pageable pageable,
                                                                    @RequestParam(required = false) String[] category){
        CategoryPageResponseDTO categoryPageResponseDTO = this.categoryService.fetchAllCategories(pageable, category, false);
        return ResponseEntity.ok().body(categoryPageResponseDTO);
    }

    // Admin endpoints
    @GetMapping("/admin/categories/{id}")
    @ApiMessage("Category retrieved successfully by admin")
    public ResponseEntity<CategoryResponseDTO> getCategoryByAdmin(@PathVariable Long id){
        CategoryResponseDTO categoryResponseDTO = this.categoryService.fetchCategory(id, true);
        return ResponseEntity.ok().body(categoryResponseDTO);
    }

    @GetMapping("/admin/categories")
    @ApiMessage("Categories list retrieved by admin")
    public ResponseEntity<CategoryPageResponseDTO> getAllCategoriesByAdmin(Pageable pageable,
                                                                           @RequestParam(required = false) String[] category){
        CategoryPageResponseDTO categoryPageResponseDTO = this.categoryService.fetchAllCategories(pageable, category, true);
        return ResponseEntity.ok().body(categoryPageResponseDTO);
    }
}