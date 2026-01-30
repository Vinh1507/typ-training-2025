package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@NoArgsConstructor
@AllArgsConstructor
public class PublisherResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updateAt;
}
