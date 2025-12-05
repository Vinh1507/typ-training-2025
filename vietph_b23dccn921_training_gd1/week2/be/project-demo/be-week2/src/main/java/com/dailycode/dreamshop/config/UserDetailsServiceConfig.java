package com.dailycode.dreamshop.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dailycode.dreamshop.model.User;
import com.dailycode.dreamshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceConfig implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByEmail(username)
            .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
            List<GrantedAuthority> listAuth= new ArrayList<>();
            listAuth.add(new SimpleGrantedAuthority("ROLE_"+ user.getRole().toString()));
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                listAuth
            );

    }
    
}
