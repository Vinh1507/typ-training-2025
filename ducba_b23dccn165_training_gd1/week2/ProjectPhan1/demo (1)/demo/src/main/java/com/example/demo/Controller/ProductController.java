package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private List<Product> products = new ArrayList<>();
    private Long id = 1L;

    public ProductController() {
        products.add(new Product(id++, "Sản phẩm 1", 15000));
        products.add(new Product(id++, "Sản phẩm 2", 20000));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return products.stream().filter(pr -> pr.getId().equals(id)).findFirst().map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createNewProduct(@RequestBody Product product) {
        product.setId(id++);
        products.add(product); // Lấy product truyền thông qua phương thức PostMapping test trong ảnh dưới với
                               // Postman
        return ResponseEntity.status(HttpStatus.CREATED).body(product); // Trả về trạng thái 201 -> Tạo thành công và
                                                                        // hiển thị ở phần body nhận được là data JSON
                                                                        // của product
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product newProduct) {
        for (Product pr : products) {
            if (pr.getId().equals(id)) {
                pr.setName(newProduct.getName());
                pr.setPrice(newProduct.getPrice());
                return ResponseEntity.ok(pr); // Trả về newProduct dưới phần body và trạng thái 200 OK
            }
        }
        return ResponseEntity.notFound().build(); // Không tìm thấy sản phẩm với id truyền trong url
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id)); // Xóa nếu tìm đúng id
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
