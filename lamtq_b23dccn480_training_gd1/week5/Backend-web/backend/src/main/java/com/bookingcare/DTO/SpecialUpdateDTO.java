package com.bookingcare.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialUpdateDTO {
    @JsonProperty("name")
    @NotBlank(message = "SpecializationName is required")
    private String specializationName;
}
