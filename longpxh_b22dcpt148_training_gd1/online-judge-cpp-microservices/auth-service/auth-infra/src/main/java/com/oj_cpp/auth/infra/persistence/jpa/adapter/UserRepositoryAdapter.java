package com.oj_cpp.auth.infra.persistence.jpa.adapter;

import com.oj_cpp.auth.domain.model.User;
import com.oj_cpp.auth.domain.repository.UserRepository;
import com.oj_cpp.auth.infra.persistence.jpa.entity.UserJpa;
import com.oj_cpp.auth.infra.persistence.jpa.mapper.UserMapper;
import com.oj_cpp.auth.infra.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpa jpa;
        if (user.getId() != null) {
            jpa = jpaRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
            mapper.updateJpaFromDomain(jpa, user);
        } else {
            jpa = mapper.toJpa(user);
        }
        UserJpa saved = jpaRepository.save(jpa);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
