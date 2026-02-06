package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Security.PermissionRequest;
import com.BTL_JAVA.BTL.DTO.Response.Security.PermissionResponse;
import com.BTL_JAVA.BTL.Entity.Permission;
import com.BTL_JAVA.BTL.Repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;

   public PermissionResponse create(PermissionRequest request) {
        Permission  permission = new Permission();
        permission.setNamePermission(request.getNamePermission());
        permission.setDescription(request.getDescription());
        permissionRepository.save(permission);
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setName(request.getNamePermission());
        permissionResponse.setDescription(request.getDescription());
        return permissionResponse;
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(p-> new PermissionResponse(p.getNamePermission(),p.getDescription())).toList();

    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
