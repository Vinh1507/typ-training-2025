package com.example.demo.Controller;

import com.example.demo.Entity.Product;
import com.example.demo.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Product p = productService.getProduct(id);
        if (p == null) return ResponseEntity.status(404).body(Map.of("message", "Product not found"));
        return ResponseEntity.ok(Map.of("data", p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        BigDecimal price = new BigDecimal(body.get("price").toString());

        Product p = productService.updateProduct(id, name, price);
        if (p == null) return ResponseEntity.status(404).body(Map.of("message", "Product not found"));
        return ResponseEntity.ok(Map.of("message", "Updated. Cache invalidated.", "data", p));
    }
}
