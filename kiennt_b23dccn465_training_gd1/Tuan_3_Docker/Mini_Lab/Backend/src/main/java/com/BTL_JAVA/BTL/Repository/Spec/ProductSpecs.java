package com.BTL_JAVA.BTL.Repository.Spec;

import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;

public class ProductSpecs {
    private ProductSpecs(){}

    public static Specification<Product> keyword(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            query.distinct(true); // tránh duplicate khi join
            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    public static Specification<Product> priceBetween(Double min, Double max) {
        if (min == null && max == null) return null;
        return (root, query, cb) -> {
            query.distinct(true);
            if (min != null && max != null)  return cb.between(root.get("price"), min, max);
            if (min != null)                  return cb.greaterThanOrEqualTo(root.get("price"), min);
            /* max != null */                 return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<Product> variationSizeIn(List<String> sizes) {
        if (sizes == null || sizes.isEmpty()) return null;
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Product, ProductVariation> v = root.join("productVariations", JoinType.INNER);
            return v.get("size").in(sizes);
        };
    }

    public static Specification<Product> variationColorIn(List<String> colors) {
        if (colors == null || colors.isEmpty()) return null;
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Product, ProductVariation> v = root.join("productVariations", JoinType.INNER);
            return v.get("color").in(colors);
        };
    }

    public static Specification<Product> variationColorContains(List<String> colorTerms) {
        if (colorTerms == null || colorTerms.isEmpty()) return null;
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Product, ProductVariation> v = root.join("productVariations", JoinType.INNER);

            var likes = colorTerms.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> "%" + s.toLowerCase() + "%")
                    .map(pat -> cb.like(cb.lower(v.get("color")), pat))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new);

            return cb.or(likes);
        };
    }
}
