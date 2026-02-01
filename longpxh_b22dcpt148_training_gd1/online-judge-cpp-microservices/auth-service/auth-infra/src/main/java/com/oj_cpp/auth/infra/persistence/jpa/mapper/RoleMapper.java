package com.oj_cpp.auth.infra.persistence.jpa.mapper;

import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.infra.persistence.jpa.entity.RoleJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    
    Role toDomain(RoleJpa jpa);
    
    RoleJpa toJpa(Role domain);
    
    void updateJpaFromDomain(@MappingTarget RoleJpa jpa, Role domain);
}
