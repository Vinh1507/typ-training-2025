package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.Product.CategoryUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.ProductInCategoryResponse;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.CategoryCreationRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.CategoryResponse;
import com.BTL_JAVA.BTL.Entity.Product.Category;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.CategoryRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.ProductSaleRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
      CategoryRepository categoryRepository;
      UploadImageFile uploadImageFile;
      ProductRepository productRepository;
      ProductVariationRepository productVariationRepository;
      ProductSaleRepository productSaleRepository;

      public ApiResponse<CategoryResponse> create(CategoryCreationRequest request) throws IOException {
          Category cat=Category.builder()
                  .name(request.getCategoryName())
                  .parent_id(request.getPerentId() == null ? 0 : request.getPerentId())
                  .build();
          //upload anh neu co

          if(request.getImage()!=null && !request.getImage().isEmpty()){
              var up=uploadImageFile.uploadImage(request.getImage());
              cat.setImageUrl(up);
              cat.setImagePublicId(up);
          }

          Category saved=categoryRepository.save(cat);

          Set<Integer> ids=(request.getProductIds()==null)?Set.of():request.getProductIds();
          if(!ids.isEmpty()){
              var prods=productRepository.findAllById(ids);
              Set<Integer> found = prods.stream().map(Product::getProductId).collect(Collectors.toSet());
              Set<Integer> missing = ids.stream().filter(i -> !found.contains(i)).collect(Collectors.toSet());
              if (!missing.isEmpty()) {
                  throw new AppException(ErrorCode.PRODUCT_NOT_FOUND,"Product không tồn tại: " + missing);
              }
              // BÊN SỞ HỮU QUAN HỆ là Product -> setCategory
              prods.forEach(p -> p.setCategory(saved));
              productRepository.saveAll(prods);
          }

          return ApiResponse.ok(toResponse(saved));

      }

    public ApiResponse<Void> delete(Integer id) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Không cho xoá khi còn sản phẩm
        if (cat.getProducts() != null && !cat.getProducts().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        categoryRepository.delete(cat);
        return ApiResponse.ok(null);
    }

    public ApiResponse<CategoryResponse> get(Integer id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Set<Integer> productIds = (c.getProducts() == null)
                ? Set.of()
                : c.getProducts().stream().map(Product::getProductId).collect(Collectors.toSet());


        return ApiResponse.ok(toResponse(c));
    }

    public ApiResponse<List<CategoryResponse>> list() {
        var categories = categoryRepository.findAll();

        var data = categories.stream().map(c -> {
            Set<Integer> productIds = (c.getProducts() == null)
                    ? Set.of()
                    : c.getProducts().stream().map(Product::getProductId).collect(Collectors.toSet());
            return toResponse(c);
        }).toList();

        return ApiResponse.ok(data);
    }

    public ApiResponse<CategoryResponse> update(Integer id, CategoryUpdateRequest request) throws IOException {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getCategoryName() != null) cat.setName(request.getCategoryName());
        if (request.getParentId() != null)     cat.setParent_id(request.getParentId());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(request.getImage());
            cat.setImageUrl(url);
        }

        if (request.getAddProductIds() != null && !request.getAddProductIds().isEmpty()) {
            var prodsToAdd = productRepository.findAllById(request.getAddProductIds());
            var found = prodsToAdd.stream().map(Product::getProductId).collect(Collectors.toSet());
            var missing = request.getAddProductIds().stream().filter(id2 -> !found.contains(id2)).toList();
            if (!missing.isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND,"Product không tồn tại: " + missing);
            }
            prodsToAdd.forEach(p -> p.setCategory(cat));  // gán về category này
            productRepository.saveAll(prodsToAdd);
        }

        if (request.getRemoveProductIds() != null && !request.getRemoveProductIds().isEmpty()) {
            var prodsToRemove = productRepository.findAllById(request.getRemoveProductIds());
            // chỉ gỡ những product hiện đang thuộc category này
            prodsToRemove.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().getId() == cat.getId())
                    .forEach(p -> p.setCategory(null));
            productRepository.saveAll(prodsToRemove);
        }

        Category saved = categoryRepository.save(cat);

        // chuẩn bị response
        Set<Integer> currentIds = (saved.getProducts() == null) ? Set.of()
                : saved.getProducts().stream().map(Product::getProductId).collect(Collectors.toSet());

        return ApiResponse.ok(toResponse(saved));
    }


    private CategoryResponse toResponse(Category c) {
        List<ProductInCategoryResponse> products = (c.getProducts() == null) ? List.of()
                : c.getProducts().stream().map(p -> {

            // Lấy danh sách ảnh từ các variation của product
            List<String> variationImages = (p.getProductVariations() == null) ? List.of()
                    : p.getProductVariations().stream()
                    .map(ProductVariation::getImage)
                    .filter(Objects::nonNull)
                    .toList();

            // Lấy sale active (nếu có) -> saleValue
            var now = java.time.LocalDateTime.now();
            var ps = productSaleRepository
                    .findActiveProductSaleByProductId(p.getProductId(), now)
                    .stream().findFirst().orElse(null); // vì đảm bảo chỉ có 1 active
            var saleValue = (ps == null) ? null : ps.getSaleValue();

            return ProductInCategoryResponse.builder()
                    .productId(p.getProductId())
                    .title(p.getTitle())
                    .price(p.getPrice())
                    .image(p.getImage())
                    .saleValue(saleValue)
                    .createdAt(p.getCreatedAt())
                    .variationCount(variationImages.size())
                    .variationImages(variationImages)
                    .build();
        }).toList();

        return CategoryResponse.builder()
                .categoryId(c.getId())
                .categoryName(c.getName())
                .parentId(c.getParent_id())
                .image(c.getImageUrl())
                .productCount(products.size())
                .products(products)
                .build();
    }
}
