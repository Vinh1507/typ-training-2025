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
public class ProductVariationGroup {
    Integer productId;
    String image;
    String color;
    List<SizeItem> list;
}
