package com.oj_cpp.auth.infra.persistence.jpa.mapper;

import com.oj_cpp.auth.domain.model.Permission;
import com.oj_cpp.auth.infra.persistence.jpa.entity.PermissionJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    
    Permission toDomain(PermissionJpa jpa);
    
    PermissionJpa toJpa(Permission domain);
    
    void updateJpaFromDomain(@MappingTarget PermissionJpa jpa, Permission domain);
}
