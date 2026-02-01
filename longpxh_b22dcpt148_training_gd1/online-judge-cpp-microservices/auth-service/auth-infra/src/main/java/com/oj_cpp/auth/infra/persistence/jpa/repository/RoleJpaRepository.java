package com.oj_cpp.auth.infra.persistence.jpa.repository;

import com.oj_cpp.auth.infra.persistence.jpa.entity.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpa, Long> {
    
    Optional<RoleJpa> findByName(String name);
    
    @Query("SELECT r FROM RoleJpa r JOIN r.userRoles ur WHERE ur.user.id = :userId")
    List<RoleJpa> findByUserId(@Param("userId") Long userId);
}
