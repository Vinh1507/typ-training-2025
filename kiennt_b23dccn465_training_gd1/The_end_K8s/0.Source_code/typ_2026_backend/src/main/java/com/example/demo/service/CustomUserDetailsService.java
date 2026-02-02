package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AccRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccRoleRepository accRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Account not found"));

        // Load roles from acc_role table
        var authorities = accRoleRepository.findByAccount(account).stream()
                .map(accRole -> new SimpleGrantedAuthority("ROLE_" + accRole.getRole().name()))
                .collect(Collectors.toList());

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(authorities)
                .disabled(!account.isEnabled())
                .build();
    }
}
