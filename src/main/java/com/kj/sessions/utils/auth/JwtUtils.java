package com.kj.sessions.utils.auth;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${sessions.jwtSecret}")
    private String jwtSecret;
    @Value("${sessions.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String usernameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error("JWT token has expired: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
