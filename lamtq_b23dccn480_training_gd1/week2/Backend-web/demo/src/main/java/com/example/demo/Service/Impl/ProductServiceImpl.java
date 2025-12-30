package com.example.demo.Service.Impl;

import com.example.demo.Converter.ConverToEntity;
import com.example.demo.Converter.ConvertToDTO;
import com.example.demo.Converter.UpdateEntityFromDTO;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ConvertToDTO convertToDTO;
    @Autowired
    private ConverToEntity converToEntity;
    @Autowired
    private UpdateEntityFromDTO updateEntityFromDTO;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntityList = productRepository.findAllByIsDeletedFalse();
        if (productEntityList.isEmpty()) {
            throw new NoSuchElementException("No products found in database.");
        }
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductEntity entity : productEntityList) {
            productDTOList.add(convertToDTO.toDTO(entity));
        }
        return productDTOList;
    }

    @Override
    public ProductDTO getProductById(long id) {
        Optional<ProductEntity> productEntity = productRepository.findByIdAndIsDeletedFalse(id);
        if(productEntity.isPresent()) {
            return convertToDTO.toDTO(productEntity.get());
        }
        throw new NoSuchElementException("No products found in database.");
    }

    @Override
    public ProductDTO getProductByName(String name) {
        Optional<ProductEntity> productEntity = productRepository.findByProductNameAndIsDeletedFalse(name);
        if(productEntity.isPresent()) {
            return convertToDTO.toDTO(productEntity.get());
        }
        throw new NoSuchElementException("No products found in database.");
    }

    @Transactional
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        ProductEntity productEntity = converToEntity.toEntity(productDTO);
        productEntity.setId(null);
        productEntity = productRepository.save(productEntity);
        return convertToDTO.toDTO(productEntity);
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        ProductEntity productEntity = converToEntity.toEntity(productDTO);
        updateEntityFromDTO.updateProductEntity(productDTO, productEntity);
        productEntity = productRepository.save(productEntity);
        return convertToDTO.toDTO(productEntity);
    }

    @Transactional
    @Override
    public void deleteProduct(long id) {
        // Xoa mem
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + id));

        try {
            productEntity.setDeleted(true);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Failed to delete product: " + e.getMessage(), e);
        }
    }
}
