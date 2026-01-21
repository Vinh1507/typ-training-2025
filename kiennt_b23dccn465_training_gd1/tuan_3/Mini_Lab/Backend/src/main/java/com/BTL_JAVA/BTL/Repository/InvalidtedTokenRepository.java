package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.InvalidtedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidtedTokenRepository extends JpaRepository<InvalidtedToken, String> {
}
