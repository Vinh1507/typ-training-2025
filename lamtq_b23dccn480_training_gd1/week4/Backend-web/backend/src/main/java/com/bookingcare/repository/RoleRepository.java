package com.bookingcare.repository;

import com.bookingcare.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleCode(String roleCode);
}
