package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Security.RoleRequest;
import com.BTL_JAVA.BTL.DTO.Response.Security.PermissionResponse;
import com.BTL_JAVA.BTL.DTO.Response.Security.RoleResponse;
import com.BTL_JAVA.BTL.Entity.Permission;
import com.BTL_JAVA.BTL.Entity.Role;
import com.BTL_JAVA.BTL.Repository.PermissionRepository;
import com.BTL_JAVA.BTL.Repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
     RoleRepository roleRepository;

     PermissionRepository permissionRepository;

     public RoleResponse create(RoleRequest request) {
         Role  role = new Role();
         role.setDescription(request.getDescription());
         role.setNameRoles(request.getNameRoles());

         List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
         role.setPermissions(new HashSet<>(permissions));
         Role saved = roleRepository.save(role);

         // 4) Map sang DTO trả về
         Set<PermissionResponse> permDtos =
                 role.getPermissions().stream()
                         .map(p -> new PermissionResponse(p.getNamePermission(), p.getDescription()))
                         .collect(Collectors.toSet()); // KHÔNG sửa role.getPermissions() ở trong stream này


         RoleResponse dto = new RoleResponse();
         dto.setName(saved.getNameRoles());
         dto.setDescription(saved.getDescription());
         dto.setPermissions(permDtos);

         return dto;

     }

     public List<RoleResponse> getAll() {
         var roles = roleRepository.findAll();
         return roles.stream()
                 .map(this::toRoleResponse)
                 .toList();
     }
     public void delete(String role){
         roleRepository.deleteById(role);
     }

     private RoleResponse toRoleResponse(Role role) {
        // Map Set<Permission> -> Set<PermissionResponse>, giữ thứ tự chèn
        Set<PermissionResponse> permDtos = role.getPermissions().stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toSet());

        RoleResponse dto = new RoleResponse();
        dto.setName(role.getNameRoles());
        dto.setDescription(role.getDescription());
        dto.setPermissions(permDtos);
        return dto;
    }
    private PermissionResponse toPermissionResponse(Permission p) {
        PermissionResponse dto = new PermissionResponse();
        dto.setName(p.getNamePermission());
        dto.setDescription(p.getDescription());
        return dto;
    }



}
