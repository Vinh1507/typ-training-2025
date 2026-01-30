package ptit.edu.vn.bookshop.domain.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private String receiverName;
    private String phone;
    private String city;
    private String district;
    private String ward;
    private String street;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Boolean isDefault;
    private Instant createdAt;
    private Instant updateAt;
}
