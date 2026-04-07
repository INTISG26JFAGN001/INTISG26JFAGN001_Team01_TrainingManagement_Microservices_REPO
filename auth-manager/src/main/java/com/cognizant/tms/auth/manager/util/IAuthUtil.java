package com.cognizant.tms.auth.manager.util;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IAuthUtil {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    Claims parseClaims(String token);
    String parseSubject(String token);
    List<String> parseRoles(String token);
    boolean validToken(String token);
}
