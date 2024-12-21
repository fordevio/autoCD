package com.fordevio.producer.payloads.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class UnauthorizeResponse {
 
    public void sendUnauthorizedResponse(HttpServletRequest request,HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
    response.setContentType("application/json");

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", message);
    body.put("path",request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);

    response.getOutputStream().flush(); 
}
}
