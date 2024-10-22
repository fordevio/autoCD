package com.fordevio.producer.services.security.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                sendUnauthorizedResponse(response, "Missing JWT token");
                return;
             }
            }catch(Exception e){
              log.warn("Jwt exception:",e);
              sendUnauthorizedResponse(response, e.getMessage());
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

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
    response.setContentType("application/json");

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", message);
  

    // Convert the response body to JSON and write it to the output stream
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);

    response.getOutputStream().flush(); // Ensure the response is written out
}

}
