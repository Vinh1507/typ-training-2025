package com.BTL_JAVA.BTL.DTO.Request.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderRequest {
    String note;

    @NotEmpty
    @Valid
    List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {
        @NotNull
        Integer variationId;

        @NotNull
        @Min(1)
        Integer quantity;
    }
}
