package ptit.edu.vn.bookshop.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String country;
    private String biography;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updateAt;
}
