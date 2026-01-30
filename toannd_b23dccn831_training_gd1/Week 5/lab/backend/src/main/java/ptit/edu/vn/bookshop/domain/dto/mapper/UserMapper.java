package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ptit.edu.vn.bookshop.domain.dto.request.UserCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.auth.RegisterRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.UserResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "role.id", source = "role.id", defaultExpression = "java(null)")
    })
    User toEntity(UserCreateRequestDTO dto);

    @Mappings({
            @Mapping(target = "avatarUrl", source = "avatar"),
            @Mapping(target = "role.id", source = "role.id"),
            @Mapping(target = "role.name", source = "role.name")
    })
    UserResponseDTO toResponseDto(User user);

    User fromRegisterDto(RegisterRequestDTO dto);
}
