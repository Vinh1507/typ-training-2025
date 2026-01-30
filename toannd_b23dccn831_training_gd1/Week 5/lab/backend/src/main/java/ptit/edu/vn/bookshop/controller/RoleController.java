package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.RoleCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.RoleUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.RoleResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.RolePageResponseDTO;
import ptit.edu.vn.bookshop.service.RoleService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Role created successfully")
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleCreateRequestDTO roleCreateRequestDTO) {
        RoleResponseDTO roleResponseDTO = this.roleService.createRole(roleCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleResponseDTO);
    }

    @PutMapping("/roles/{id}")
    @ApiMessage("Role updated successfully")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequestDTO roleRequestDTO) {
        RoleResponseDTO roleResponseDTO = this.roleService.updateRole(roleRequestDTO, id);
        return ResponseEntity.ok().body(roleResponseDTO);
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Role deleted successfully")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        this.roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Role retrieved successfully")
    public ResponseEntity<RoleResponseDTO> getRole(@PathVariable Long id) {
        RoleResponseDTO roleResponseDTO = this.roleService.fetchRole(id);
        return ResponseEntity.ok().body(roleResponseDTO);
    }

    @GetMapping("/roles")
    @ApiMessage("Roles retrieved successfully")
    public ResponseEntity<RolePageResponseDTO> getAllRoles(Pageable pageable,
                                                           @RequestParam(required = false) String[] role) {
        RolePageResponseDTO rolePageResponseDTO = this.roleService.fetchAllRoles(pageable, role);
        return ResponseEntity.ok().body(rolePageResponseDTO);
    }
}