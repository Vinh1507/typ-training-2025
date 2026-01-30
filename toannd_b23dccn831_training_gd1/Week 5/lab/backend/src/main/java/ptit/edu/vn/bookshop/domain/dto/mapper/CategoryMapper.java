package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import ptit.edu.vn.bookshop.domain.dto.request.CategoryRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CategoryResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Category;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDTO dto);
    CategoryResponseDTO toResponseDTO(Category category);
}
