package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import ptit.edu.vn.bookshop.domain.dto.request.PermissionCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.PermissionResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionCreateRequestDTO dto);
    PermissionResponseDTO toResponseDTO(Permission permission);
}
