package org.epam.gymapplication.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface IJwtTokenService {
    String generateToken(String username);
    Claims extractAllClaims(String token);
    boolean validateToken(String token, UserDetails userDetails);
    Date extractExpiration(String token);
    String extractUsername(String token);
}
