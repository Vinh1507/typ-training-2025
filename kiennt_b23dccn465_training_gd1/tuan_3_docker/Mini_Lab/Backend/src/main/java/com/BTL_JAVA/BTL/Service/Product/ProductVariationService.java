package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.ProductVariationCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.ProductVariationUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.ProductVariationResponse;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariationService {
    ProductVariationRepository productVariationRepository;
    ProductRepository productRepository;
    UploadImageFile uploadImageFile;

    public ApiResponse<ProductVariationResponse> create(ProductVariationCreationRequest req) throws IOException {
        if (req.getProductId() == null||req.getSize()==null||req.getColor()==null) {
            throw new AppException(ErrorCode.INVALID_VARIATION);
        }

        if(productVariationRepository.existsByProduct_ProductIdAndSizeIgnoreCaseAndColorIgnoreCase(req.getProductId(), req.getSize(), req.getColor())) {
            throw new AppException(ErrorCode.DUPLICATE_VARIATION);
        }

        Product productRef = productRepository.getReferenceById(req.getProductId());

        ProductVariation pv = ProductVariation.builder()
                .product(productRef)
                .size(req.getSize())
                .color(req.getColor())
                .stockQuantity(req.getStockQuantity() == null ? 0 : req.getStockQuantity())
                .build();

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            pv.setImage(url);
        }

        ProductVariation saved = productVariationRepository.save(pv);
        return ApiResponse.ok(toResponse(saved));
    }

    // UPDATE (partial + có thể chuyển sang product khác)
    @Transactional
    public ApiResponse<ProductVariationResponse> update(Integer id, ProductVariationUpdateRequest req) throws IOException {

        ProductVariation pv = productVariationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIATION_NOT_FOUND));

        Integer productId = req.getProductId() != null ? req.getProductId()
                : (pv.getProduct() != null ? pv.getProduct().getProductId() : null);

        String size  = req.getSize()  != null ? req.getSize().trim()  : pv.getSize();
        String color = req.getColor() != null ? req.getColor().trim() : pv.getColor();

        if (productVariationRepository
                .existsByProduct_ProductIdAndSizeIgnoreCaseAndColorIgnoreCaseAndIdNot(productId, size, color, id)) {
            throw new AppException(ErrorCode.DUPLICATE_VARIATION);
        }


        if (req.getProductId() != null) {
            Product productRef = productRepository.getReferenceById(req.getProductId());
            pv.setProduct(productRef);
        }
        if (req.getSize() != null)          pv.setSize(req.getSize());
        if (req.getColor() != null)         pv.setColor(req.getColor());
        if (req.getStockQuantity() != null) pv.setStockQuantity(req.getStockQuantity());

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            pv.setImage(url);
        }

        ProductVariation saved = productVariationRepository.save(pv);
        return ApiResponse.ok(toResponse(saved));
    }


    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        ProductVariation pv = productVariationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIATION_NOT_FOUND));
        productVariationRepository.delete(pv);
        return ApiResponse.ok(null);
    }



    public ApiResponse<ProductVariationResponse> get(Integer id) {
        ProductVariation pv = productVariationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIATION_NOT_FOUND));
        return ApiResponse.ok(toResponse(pv));
    }


    public ApiResponse<List<ProductVariationResponse>> list() {
        var all = productVariationRepository.findAll();
        var data = all.stream().map(this::toResponse).toList();
        return ApiResponse.ok(data);
    }


    private ProductVariationResponse toResponse(ProductVariation pv) {
        return ProductVariationResponse.builder()
                .id(pv.getId())
                .productId(pv.getProduct() == null ? null : pv.getProduct().getProductId())
                .size(pv.getSize())
                .color(pv.getColor())
                .stockQuantity(pv.getStockQuantity())
                .image(pv.getImage())
                .build();
    }
}

