package com.BTL_JAVA.BTL.DTO.Request.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
    private String categoryName;

    private Integer perentId;


    private MultipartFile image;

    Set<Integer> productIds;

}
