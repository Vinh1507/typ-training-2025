package com.dailycode.dreamshop.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dailycode.dreamshop.utils.jwt.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class JwtAuthFilterConfig extends OncePerRequestFilter {
    private final List<String> PUBLIC_URL= List.of("/auth/login","/auth/register","/auth/refresh-token");
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceConfig userDetailsServiceConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            try {
                String path = request.getServletPath();
                if(PUBLIC_URL.contains(path))
                {
                    filterChain.doFilter(request, response);
                    return ;
                }
                String token = jwtProvider.getTokenFromheaders(request);
                if(token!=null && jwtProvider.validateToken(token))
                {
                    String email = jwtProvider.getEmailFromToken(token);
                    if(email != null && SecurityContextHolder.getContext().getAuthentication()==null)
                    {
                        UserDetails userDetails = userDetailsServiceConfig.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
    
                    }
                }
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
    }
    
}
