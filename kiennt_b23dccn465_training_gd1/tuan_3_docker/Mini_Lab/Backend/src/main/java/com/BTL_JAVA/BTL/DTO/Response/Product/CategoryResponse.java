package com.BTL_JAVA.BTL.DTO.Response.Product;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
        Integer categoryId;
        String categoryName;
        Integer parentId;
        String image;
        Integer   productCount;
        List<ProductInCategoryResponse> products;
}

