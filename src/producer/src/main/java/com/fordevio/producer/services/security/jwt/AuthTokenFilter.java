package com.fordevio.producer.services.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fordevio.producer.payloads.response.UnauthorizeResponse;
import com.fordevio.producer.services.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter{
        
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UnauthorizeResponse unauthorizeResponse;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain chain) throws ServletException, IOException{
  
      
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/protected/")){
            String jwt = parseJwt(request);
            try{
                if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                    String username= jwtUtils.getUsernameFromJwtToken(jwt);
                    UserDetails userDetails= userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
             }else{
                unauthorizeResponse.sendUnauthorizedResponse(request, response, "Empty authentication token");
                return;
             }
            }catch(Exception e){
              log.warn("Jwt exception:",e);
              unauthorizeResponse.sendUnauthorizedResponse(request, response, e.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
       
    } 

    private String parseJwt(HttpServletRequest request){
        String headerAuth= request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth)&&headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

}
