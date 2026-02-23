package com.BTL_JAVA.BTL.DTO.Response.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeItem {
    Integer idVariation;
    String size;
    Integer stockQuantity;
}
