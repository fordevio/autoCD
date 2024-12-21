package com.fordevio.producer.services.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fordevio.producer.payloads.response.UnauthorizeResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    
    @Autowired
    private UnauthorizeResponse unauthorizeResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
       log.error("Unauthorized error:{}", authException);

       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
       unauthorizeResponse.sendUnauthorizedResponse(request, response, authException.getMessage());

    }
}

