package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

@Getter
public class PublisherRequestDTO {
    @NotBlank(message = "Publisher name is required")
    @Size(min = 2, max = 100, message = "Publisher name must be between 2 and 100 characters")
    private String name;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;


    private String phone;
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;


    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
