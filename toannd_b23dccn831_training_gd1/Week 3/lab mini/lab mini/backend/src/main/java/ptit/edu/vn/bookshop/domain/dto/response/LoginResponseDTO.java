package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserLogin user;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private Long id;
        private String name;
        private String email;
        private StatusEnum status;
        private UserResponseDTO.UserRoleResponseDTO role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken{
        private Long id;
        private String email;
        private String name;
        @Enumerated(EnumType.STRING)
        private StatusEnum status;
    }
}
