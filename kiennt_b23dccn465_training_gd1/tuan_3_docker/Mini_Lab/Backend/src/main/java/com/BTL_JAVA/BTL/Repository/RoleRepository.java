package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
}
