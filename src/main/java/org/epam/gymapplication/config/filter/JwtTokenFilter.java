package org.epam.gymapplication.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.epam.gymapplication.domain.dto.ExceptionResponsePayload;
import org.epam.gymapplication.exception.JwtTokenExpiredException;
import org.epam.gymapplication.service.impl.JwtTokenService;
import org.epam.gymapplication.service.impl.UserDetailsServiceImpl;
import org.epam.gymapplication.utils.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JwtTokenFilter(JwtTokenService jwtTokenService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                username = jwtTokenService.extractUsername(jwtToken);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                if (jwtTokenService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException | JwtTokenExpiredException e) {
            handleExpiredJwtException(response, new JwtTokenExpiredException(ExceptionMessage.jwtTokenExpired()));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredJwtException(HttpServletResponse response, JwtTokenExpiredException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ExceptionResponsePayload responsePayload = new ExceptionResponsePayload(HttpStatus.UNAUTHORIZED, e.getMessage());
        String jsonResponse = new ObjectMapper().writeValueAsString(
                Map.of("message", responsePayload.getMessage(),
                        "timestamp", responsePayload.getTimestamp().toString(),
                        "status", responsePayload.getStatus().toString(),
                        "errorCode", responsePayload.getStatusCode())
        );
        logger.warn("401 [JwtTokenExpiredException] - " + e.getMessage());
        response.getWriter().write(jsonResponse);
    }
}
