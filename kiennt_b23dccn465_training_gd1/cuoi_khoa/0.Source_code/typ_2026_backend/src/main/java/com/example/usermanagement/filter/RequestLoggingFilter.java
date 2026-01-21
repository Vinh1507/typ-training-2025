package com.example.usermanagement.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger("HTTP_REQUEST");
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        String requestId = Optional.ofNullable(request.getHeader("X-Request-Id"))
            .filter(v -> !v.isBlank())
            .orElseGet(() -> UUID.randomUUID().toString());
        
      
        MDC.put("request_id", requestId);
        MDC.put("method", request.getMethod());
        MDC.put("path", request.getRequestURI());
        response.setHeader("X-Request-Id", requestId);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            MDC.put("status", String.valueOf(response.getStatus()));
            MDC.put("duration_ms", String.valueOf(duration));
            
          
            logger.info("HTTP Request - Method: {}, Path: {}, Status: {}, Duration: {}ms, RequestId: {}", 
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration,
                requestId
            );
            
          
            MDC.remove("request_id");
            MDC.remove("method");
            MDC.remove("path");
            MDC.remove("status");
            MDC.remove("duration_ms");
        }
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
     
        return path.startsWith("/actuator") || 
               path.equals("/health") || 
               path.equals("/favicon.ico");
    }
}