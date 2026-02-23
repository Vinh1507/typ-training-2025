package com.example.demo.repository;

import com.example.demo.model.AccRole;
import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccRoleRepository extends JpaRepository<AccRole, Long> {
    List<AccRole> findByAccount(Account account);
}
