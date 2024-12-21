package com.fordevio.producer.services.security.auth;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fordevio.producer.payloads.response.UnauthorizeResponse;
import com.fordevio.producer.services.security.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorityFilter extends OncePerRequestFilter{


    @Autowired
    private UnauthorizeResponse unauthorizeResponse;

    @Override 
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain chain) throws IOException, ServletException {
        log.info("Request to: {}", request.getRequestURI());
        String requestURI = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            
            if (principal instanceof UserDetailsImpl userDetails) {
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                if (requestURI.contains("admin")) {
                    boolean hasAdminAuthority = authorities.stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    
                    if (!hasAdminAuthority) {
                        unauthorizeResponse.sendUnauthorizedResponse(request, response, "You do not have the required authority to access this resource.");
                        return;
                    }
                }
                if (requestURI.contains("update") || requestURI.contains("edit") || requestURI.contains("delete") || requestURI.contains("create") || requestURI.contains("add")) {
                    boolean hasWriteAuthority = authorities.stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("WRITE"));
    
                    if (!hasWriteAuthority) {
                        unauthorizeResponse.sendUnauthorizedResponse(request, response, "You do not have the required authority to access this resource.");
                        return;
                    }
                }
    
                if (requestURI.contains("delete") || requestURI.contains("remove")) {
                    boolean hasDeleteAuthority = authorities.stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("DELETE"));
    
                    if (!hasDeleteAuthority) {
                        unauthorizeResponse.sendUnauthorizedResponse(request, response, "You do not have the required authority to access this resource.");
                        return;
                    }
                }
            }          
        }
        chain.doFilter(request, response);
    }
    
}
