package com.example.demo.Service;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    private final RedisService redis;
    private final ObjectMapper objectMapper;

    @Value("${app.cache.ttl.product-seconds}")
    private int productTtl;

    @Value("${app.cache.ttl.null-seconds}")
    private int nullTtl;

    @Value("${app.cache.ttl.jitter-seconds}")
    private int jitter;

    public ProductService(ProductRepository productRepo, RedisService redis, ObjectMapper objectMapper) {
        this.productRepo = productRepo;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    private String key(Long id) { return "cache:product:" + id; }

    public Product getProduct(Long id) {
        String k = key(id);

        var cached = redis.get(k);
        if (cached.isPresent()) {
            Object v = cached.get();
            if (redis.isNullMarker(v)) return null;

            return objectMapper.convertValue(v, Product.class); // FIX
        }

        var p = productRepo.findById(id).orElse(null);
        if (p == null) {
            redis.setNull(k, Duration.ofSeconds(nullTtl), jitter);
            return null;
        }

        redis.set(k, p, Duration.ofSeconds(productTtl), jitter);
        return p;
    }

    public Product updateProduct(Long id, String name, BigDecimal price) {
        var p = productRepo.findById(id).orElse(null);
        if (p == null) return null;

        p.setName(name);
        p.setPrice(price);
        var saved = productRepo.save(p);

        redis.del(key(id));
        return saved;
    }
}
