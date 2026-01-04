package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByFullName(String username);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.fullName = :fullName")
    Optional<User> findByFullName(@Param("fullName") String fullName);

}
