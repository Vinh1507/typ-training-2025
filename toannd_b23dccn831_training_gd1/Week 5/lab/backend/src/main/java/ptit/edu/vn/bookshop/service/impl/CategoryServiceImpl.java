package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.CategoryRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CategoryResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.CategoryPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Category;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.repository.CategoryRepository;
import ptit.edu.vn.bookshop.repository.specification.CategorySpecificationBuilder;
import ptit.edu.vn.bookshop.service.CategoryService;
import ptit.edu.vn.bookshop.domain.dto.mapper.CategoryMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        if (this.categoryRepository.existsByName(categoryRequestDTO.getName())) {
            throw new DataIntegrityViolationException("Category already exists with same name!");
        }
        Category category = this.categoryMapper.toEntity(categoryRequestDTO);
        return this.categoryMapper.toResponseDTO(this.categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new IdInvalidException("Category does not exist!");
        }
        Category category = categoryOptional.get();
        if (categoryRequestDTO.getName() != null) category.setName(categoryRequestDTO.getName());
        if (categoryRequestDTO.getDescription() != null) category.setDescription(categoryRequestDTO.getDescription());
        if (categoryRequestDTO.getStatus() != null) category.setStatus(categoryRequestDTO.getStatus());
        return this.categoryMapper.toResponseDTO(this.categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO fetchCategory(Long id, boolean isAdmin) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Category does not exist"));
        if(!isAdmin && category.getStatus().equals(StatusEnum.DELETED)){
            throw new IllegalStateException("Category does not exist");
        }
        return this.categoryMapper.toResponseDTO(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Category does not exist"));
        category.setStatus(StatusEnum.DELETED);
        this.categoryRepository.save(category);
    }

    @Override
    public CategoryPageResponseDTO fetchAllCategories(Pageable pageable, String[] category, boolean isAdmin) {
        CategorySpecificationBuilder builder = new CategorySpecificationBuilder();
        if (category != null && category.length > 0) {
            for (String p : category) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(p);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5));
                }
            }
        }
        if (!isAdmin) {
            builder.with("status", ":", StatusEnum.ACTIVE.name(), "", "");
        }
        Page<Category> categoryPage = this.categoryRepository.findAll(builder.build(), pageable);
        CategoryPageResponseDTO responseDTO = new CategoryPageResponseDTO();
        responseDTO.setPage(pageable.getPageNumber() + 1);
        responseDTO.setPageSize(pageable.getPageSize());
        responseDTO.setTotal(categoryPage.getTotalElements());
        responseDTO.setPages(categoryPage.getTotalPages());
        responseDTO.setCategories(categoryPage.stream().map(categoryMapper::toResponseDTO).collect(Collectors.toList()));
        return responseDTO;
    }

}
