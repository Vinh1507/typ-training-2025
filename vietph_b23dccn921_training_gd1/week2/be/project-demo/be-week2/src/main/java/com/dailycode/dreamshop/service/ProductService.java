package com.dailycode.dreamshop.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dailycode.dreamshop.dto.AddOrUpdatProductRequest;
import com.dailycode.dreamshop.dto.DataResponse;
import com.dailycode.dreamshop.model.Product;
import com.dailycode.dreamshop.repository.ProductRepository;
import com.dailycode.dreamshop.utils.error.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // private final ArrayList<Product> productsList= new ArrayList<>();
    private Product createProduct(AddOrUpdatProductRequest request) {
        Product product = new Product();
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return product;
    }

    public DataResponse addProduct(AddOrUpdatProductRequest request) {
        Product product = createProduct(request);
        productRepository.save(product);
        return new DataResponse(HttpStatus.CREATED, true, "Product added", product);
    }

    public DataResponse getAll() {
        return new DataResponse(HttpStatus.OK, true, null, productRepository.findAll());
    }

    public DataResponse getOne(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        return new DataResponse(HttpStatus.OK, true, null, product);
    }

    public DataResponse updateOne(Long id, AddOrUpdatProductRequest updateProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        product.setName(updateProduct.getName());
        product.setBrand(updateProduct.getBrand());
        product.setDescription(updateProduct.getDescription());
        product.setPrice(updateProduct.getPrice());
        product.setQuantity(updateProduct.getQuantity());
        productRepository.save(product);
        return new DataResponse(HttpStatus.OK, true, "Product updated", product);
    }

    public void deleteOne(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> new NotFoundException("Product not found!"));
    }

    public DataResponse getProductsByPrice(BigDecimal price) {
        return new DataResponse(HttpStatus.OK, true, null, productRepository.findProductsByPrice(price));
    }

}
