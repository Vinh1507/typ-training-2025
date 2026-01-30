package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.PermissionCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.PermissionUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.PermissionResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.PermissionPageResponseDTO;
import ptit.edu.vn.bookshop.service.PermissionService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Permission created successfully")
    public ResponseEntity<PermissionResponseDTO> createPermission(@Valid @RequestBody PermissionCreateRequestDTO permissionCreateRequestDTO){
        PermissionResponseDTO permissionResponseDTO = this.permissionService.createPermission(permissionCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionResponseDTO);
    }

    @PutMapping("/permissions/{id}")
    @ApiMessage("Permission updated successfully")
    public ResponseEntity<PermissionResponseDTO> updatePermission(@Valid @RequestBody PermissionUpdateRequestDTO permissionRequestDTO, @PathVariable Long id){
        PermissionResponseDTO permissionResponseDTO = this.permissionService.updatePermission(permissionRequestDTO, id);
        return ResponseEntity.ok().body(permissionResponseDTO);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Permission deleted successfully")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id){
        this.permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Permission retrieved successfully")
    public ResponseEntity<PermissionResponseDTO> getPermission(@PathVariable Long id){
        PermissionResponseDTO permissionResponseDTO = this.permissionService.fetchPermission(id);
        return ResponseEntity.ok().body(permissionResponseDTO);
    }

    @GetMapping("/permissions")
    @ApiMessage("Permissions retrieved successfully")
    public ResponseEntity<PermissionPageResponseDTO> getAllPermission(Pageable pageable, @RequestParam(required = false) String[] permission){
        PermissionPageResponseDTO responseDTO = this.permissionService.getAllPermission(pageable, permission);
        return  ResponseEntity.ok().body(responseDTO);
    }
}