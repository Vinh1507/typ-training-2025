package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
}
