package com.cognizant.tms.auth.manager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthUtil implements IAuthUtil{
    @Value("${jwt.auth-gen.secret-key}")
    private String key;

    @Value("${jwt.auth-gen.accessKey.expirationTimeInSeconds}")
    private long accessKeyExpirationTime;

    @Value("${jwt.auth-gen.refreshKey.expirationTimeInSeconds}")
    private long refreshKeyExpirationTime;

    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("claims",userDetails.getAuthorities().stream()
                .map(auth->auth.getAuthority())
                .toList());
        return Jwts.builder()
                .claims(claims)
                .setSubject(userDetails.getUsername())
                .issuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+accessKeyExpirationTime*1000))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("claims",userDetails.getAuthorities().stream()
                .map(auth->auth.getAuthority())
                .toList());
        return Jwts.builder()
                .claims(claims)
                .setSubject(userDetails.getUsername())
                .issuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+refreshKeyExpirationTime*1000))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Claims parseClaims(String token){
        return Jwts.parser().setSigningKey(key).build().parseSignedClaims(token).getPayload();
    }

    public String parseSubject(String token){
        return parseClaims(token).getSubject();
    }

    public List<String> parseRoles(String token){
        return (List<String>) parseClaims(token).get("claims");
    }

    public boolean validToken(String token){
        try{
            Jwts.parser().setSigningKey(key).build().parseSignedClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
