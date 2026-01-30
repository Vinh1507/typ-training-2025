package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private String description;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private List<RolePermissionResponseDTO> permissions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolePermissionResponseDTO {
        private Long id;
        private String name;
    }

}
