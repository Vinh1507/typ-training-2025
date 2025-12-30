package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("productName")
    @NotBlank(message = "Name is required")
    private String productName;

    @JsonProperty("price")
    private double price;

    @JsonProperty("quantity")
    private long quantity;

    @JsonProperty("description")
    private String description;
}
