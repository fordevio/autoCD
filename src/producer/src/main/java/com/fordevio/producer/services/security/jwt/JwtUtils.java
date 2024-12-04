package com.fordevio.producer.services.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fordevio.producer.services.security.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
    private String jwtSecret="===============================autocd==========================";

    public String generateJwtToken(Authentication authentication, long jwtExpirationMs ) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
       
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime()+jwtExpirationMs)).signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authtoken) throws Exception {
        Jwts.parserBuilder().setSigningKey(key()).build().parse(authtoken);
        return true;       
    }

}
