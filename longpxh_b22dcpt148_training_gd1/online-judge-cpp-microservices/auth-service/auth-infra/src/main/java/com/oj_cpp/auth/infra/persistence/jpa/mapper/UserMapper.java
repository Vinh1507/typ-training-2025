package com.oj_cpp.auth.infra.persistence.jpa.mapper;

import com.oj_cpp.auth.domain.model.User;
import com.oj_cpp.auth.infra.persistence.jpa.entity.UserJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    User toDomain(UserJpa jpa);
    
    UserJpa toJpa(User domain);
    
    void updateJpaFromDomain(@MappingTarget UserJpa jpa, User domain);
}
