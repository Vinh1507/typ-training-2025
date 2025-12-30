package com.example.demo.Service;

import com.example.demo.DTO.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(long id);
    ProductDTO getProductByName(String name);
    ProductDTO addProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO productDTO);
    void  deleteProduct(long id);
}
