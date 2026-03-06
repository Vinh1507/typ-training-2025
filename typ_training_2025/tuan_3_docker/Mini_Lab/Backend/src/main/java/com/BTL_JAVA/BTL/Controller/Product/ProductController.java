package com.BTL_JAVA.BTL.Controller.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.ProductCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.ProductUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.PageResult;
import com.BTL_JAVA.BTL.DTO.Response.Product.ProductDetailResponse;
import com.BTL_JAVA.BTL.DTO.Response.Product.ProductResponse;
import com.BTL_JAVA.BTL.Service.Product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> create(@ModelAttribute ProductCreationRequest req) throws IOException {
        return ResponseEntity.ok(productService.create(req));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Integer id,
                                                               @ModelAttribute ProductUpdateRequest req) throws IOException {
        return ResponseEntity.ok(productService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> get(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResult<ProductResponse>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> sizes,   // ?sizes=S&sizes=M hoặc sizes=S,M
            @RequestParam(required = false) List<String> colors,  // ?colors=red&colors=blue hoặc colors=red,blue
            @PageableDefault(size = 5, sort = "price") Pageable pageable) { // <— mặc định 5

        return ResponseEntity.ok(
                productService.search(
                        keyword, minPrice, maxPrice,
                        normalize(sizes), normalize(colors),
                        pageable
                )
        );}

        // hỗ trợ tách CSV "S,M" thành ["S","M"]
        private List<String> normalize (List < String > in) {
            if (in == null) return null;
            return in.stream()
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
    }

