package org.epam.gymapplication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.epam.gymapplication.exception.JwtTokenExpiredException;
import org.epam.gymapplication.service.IJwtTokenService;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.epam.gymapplication.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService implements IJwtTokenService {

    private final String JWT_SECRET;

    public JwtTokenService(@Value("${jwt.secret-key}") String jwtSECRET) {
        JWT_SECRET = jwtSECRET;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Date issuetDate = getIssuetDate();
        Date expiratinDate = getExpirationDate(issuetDate);
        return Jwts
                .builder()
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(issuetDate)
                .setExpiration(expiratinDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException(ExceptionMessage.jwtTokenExpired());
        }
        return claims;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date currentDate = new Date(System.currentTimeMillis());
        return currentDate
                .after(extractExpiration(token));
    }

    private Date getIssuetDate() {
        return new Date(System.currentTimeMillis());
    }

    private Date getExpirationDate(Date inspiredDate) {
        return new Date(inspiredDate.getTime() + SecurityConstants.JWT_TOKEN_LIFE_TIME_60_MINUTES_IN_MILLIS);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token)
                .getExpiration();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    private Key decodeSecretKeyToBase64(String secretKey) {
        byte[] keyInBase64 = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyInBase64);
    }

    private Key getSignInKey() {
        return decodeSecretKeyToBase64(JWT_SECRET);
    }

}
