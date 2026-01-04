package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Address;
import com.BTL_JAVA.BTL.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("""
           select a
           from Address a
           join fetch a.user u
           where u.id = :userId and a.isDefault = true
           """)
    Optional<Address> findByUserId(@Param("userId") Integer userId);
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndIsDefaultTrue(User user);
}
