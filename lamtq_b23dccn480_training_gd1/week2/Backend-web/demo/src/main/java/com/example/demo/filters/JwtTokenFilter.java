package com.example.demo.filters;

import com.example.demo.Entity.UserEntity;
import com.example.demo.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //v1 check token de xac thuc
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            if(isBypassToken(request)) { // khong can ktra token nua ma van co the tiep tuc di ra web config
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            final String userName = jwtTokenUtil.extractUsername(token);
            // lay securityContext => lay doi tuong authentication luu thong tin nguoi dung trong context
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Người dùng chưa được xác thực => cần kiểm tra token
                UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(userName);
                if(jwtTokenUtil.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); //user chua userName, password, listRole
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // gan them thong tin phu ve request vao doi tuong nhu session id, ip
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("Authentication after set: " +
                            SecurityContextHolder.getContext().getAuthentication());
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                            .forEach(auth -> System.out.println("Authority: " + auth.getAuthority()));
                }
            }
            filterChain.doFilter(request, response); // di tiep sang filter chua requestMatcher
        }catch (Exception ex){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }
    }

    private boolean isBypassToken(HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/users/register", "POST"),
                Pair.of("/api/users/login","POST")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}

