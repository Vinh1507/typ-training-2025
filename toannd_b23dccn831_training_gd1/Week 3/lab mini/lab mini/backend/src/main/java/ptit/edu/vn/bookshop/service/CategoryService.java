package ptit.edu.vn.bookshop.service;

import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.request.CategoryRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CategoryResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.CategoryPageResponseDTO;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long id);
    CategoryResponseDTO fetchCategory(Long id, boolean isAdmin);
    void deleteCategory(Long id);
    CategoryPageResponseDTO fetchAllCategories(Pageable pageable, String[] category, boolean isAdmin);

}
