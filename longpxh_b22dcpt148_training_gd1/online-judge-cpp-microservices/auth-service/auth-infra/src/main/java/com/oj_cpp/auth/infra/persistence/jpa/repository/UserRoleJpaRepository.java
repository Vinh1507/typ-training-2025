package com.oj_cpp.auth.infra.persistence.jpa.repository;

import com.oj_cpp.auth.infra.persistence.jpa.entity.UserRoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleJpa, UserRoleJpa.UserRoleId> {
    
    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}
