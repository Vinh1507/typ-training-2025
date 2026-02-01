package com.oj_cpp.auth.infra.service;

import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.domain.repository.RoleRepository;
import com.oj_cpp.auth.domain.service.RoleService;
import com.oj_cpp.auth.infra.persistence.jpa.entity.RoleJpa;
import com.oj_cpp.auth.infra.persistence.jpa.entity.UserJpa;
import com.oj_cpp.auth.infra.persistence.jpa.entity.UserRoleJpa;
import com.oj_cpp.auth.infra.persistence.jpa.repository.RoleJpaRepository;
import com.oj_cpp.auth.infra.persistence.jpa.repository.UserJpaRepository;
import com.oj_cpp.auth.infra.persistence.jpa.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        return roleRepository.findByUserId(userId);
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        UserJpa user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RoleJpa role = roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        UserRoleJpa userRole = new UserRoleJpa();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleJpaRepository.save(userRole);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRoleJpaRepository.deleteByUserIdAndRoleId(userId, roleId);
    }
}
