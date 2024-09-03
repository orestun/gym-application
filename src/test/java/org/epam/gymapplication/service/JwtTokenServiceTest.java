package org.epam.gymapplication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.epam.gymapplication.exception.JwtTokenExpiredException;
import org.epam.gymapplication.service.impl.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Mock
    private UserDetails userDetails;

    private String JWT_SECRET = "645091ff7781b0a32d3712c897bb6688645091ff7781b0a32d3712c897bb6688";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenService = new JwtTokenService(JWT_SECRET);
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";
        String token = jwtTokenService.generateToken(username);

        assertNotNull(token);
    }

    @Test
    void testExtractAllClaims() {
        String token = jwtTokenService.generateToken("testUser");
        Claims claims = jwtTokenService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void testValidateToken_Valid() {
        String username = "testUser";
        String token = jwtTokenService.generateToken(username);

        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = jwtTokenService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Invalid() {
        String token = jwtTokenService.generateToken("testUser");

        when(userDetails.getUsername()).thenReturn("differentUser");

        boolean isValid = jwtTokenService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtTokenService.generateToken("testUser");

        Date expirationDate = jwtTokenService.extractExpiration(token);

        assertNotNull(expirationDate);
    }

    @Test
    void testExtractUsername() {
        String token = jwtTokenService.generateToken("testUser");

        String username = jwtTokenService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void testExtractAllClaims_ExpiredJwtException() {
        String expiredToken = createExpiredToken();

        assertThrows(JwtTokenExpiredException.class, () -> {
            jwtTokenService.extractAllClaims(expiredToken);
        });
    }

    private String createExpiredToken() {
        Map<String, Object> claims = new HashMap<>();
        String username = "testUser";
        Date issueDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() - 1000);  // Token expired 1 second ago

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
