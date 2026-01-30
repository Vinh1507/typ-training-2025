package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private GenderEnum gender;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private String avatarUrl;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private UserRoleResponseDTO role;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleResponseDTO {
        private Long id;
        private String name;
    }
}
