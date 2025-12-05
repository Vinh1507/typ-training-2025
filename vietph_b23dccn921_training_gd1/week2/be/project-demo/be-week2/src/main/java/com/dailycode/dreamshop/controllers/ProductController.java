package com.dailycode.dreamshop.controllers;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycode.dreamshop.dto.AddOrUpdatProductRequest;
import com.dailycode.dreamshop.dto.DataResponse;
import com.dailycode.dreamshop.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@EnableMethodSecurity
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponse> add(@RequestBody AddOrUpdatProductRequest request){
        DataResponse res= productService.addProduct(request);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    
    @GetMapping
    public ResponseEntity<DataResponse> getAll(){
        DataResponse res= productService.getAll();
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getOne(@PathVariable Long id){
        DataResponse res= productService.getOne(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> getOne(@PathVariable Long id,@RequestBody AddOrUpdatProductRequest request){
        DataResponse res= productService.updateOne(id,request);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> delete(@PathVariable Long id){
        productService.deleteOne(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @GetMapping("/query")
     public ResponseEntity<DataResponse> getByPrice(@RequestParam BigDecimal price){
        DataResponse res= productService.getProductsByPrice(price);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
