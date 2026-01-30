package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ptit.edu.vn.bookshop.domain.dto.request.RoleCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.RoleResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Role;
import ptit.edu.vn.bookshop.domain.entity.Permission;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    Role toEntity(RoleCreateRequestDTO dto);

    @Mappings({
            @Mapping(target = "permissions", source = "permissions")
    })
    RoleResponseDTO toResponseDto(Role role);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    RoleResponseDTO.RolePermissionResponseDTO toPermissionDto(Permission permission);

    List<RoleResponseDTO.RolePermissionResponseDTO> toPermissionDtoList(List<Permission> permissions);
}
