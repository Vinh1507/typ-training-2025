package com.BTL_JAVA.BTL.Controller.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Sales.SalesCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.Sales.SalesUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Sales.SalesResponse;
import com.BTL_JAVA.BTL.Service.Product.SalesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesController {

    SalesService salesService;

    // GET /sales
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesResponse>>> getAllSales(
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(salesService.getAllSales(active));
    }

    // POST /sales
    @PostMapping
    public ResponseEntity<ApiResponse<SalesResponse>> createSale(
            @RequestBody SalesCreationRequest request) {
        return ResponseEntity.ok(salesService.create(request));
    }

    // PUT /sales
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SalesResponse>> updateSale(
            @PathVariable Integer id,
            @RequestBody SalesUpdateRequest request) {
        return ResponseEntity.ok(salesService.update(id, request));  // TRUYỀN id RIÊNG
    }

    // DELETE /sales/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSale(@PathVariable Integer id) {
        return ResponseEntity.ok(salesService.delete(id));
    }
}