package ptit.edu.vn.bookshop.service;

import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.request.RoleCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.RoleUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.RolePageResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.RoleResponseDTO;

public interface RoleService {
    RoleResponseDTO createRole(RoleCreateRequestDTO roleCreateRequestDTO);
    RoleResponseDTO updateRole(RoleUpdateRequestDTO roleUpdateRequestDTO, Long id);
    RoleResponseDTO fetchRole(Long id);
    void deleteRole(Long id);
    RolePageResponseDTO fetchAllRoles(Pageable pageable, String[] role);
}
