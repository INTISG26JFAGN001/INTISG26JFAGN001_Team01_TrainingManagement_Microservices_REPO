package com.cognizant.tms.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.auth-gen.secret-key}")
    private String secret_key;

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret_key).build().parseSignedClaims(token).getPayload();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public String extractUsername(String token){
        return Jwts.parser().setSigningKey(secret_key).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public List<String> extractRoles(String token){
        return (List<String>)Jwts.parser().setSigningKey(secret_key).build().parseSignedClaims(token)
                .getPayload().get("claims");
    }
}
