package com.kj.jwt.utils.auth;

import com.kj.jwt.utils.Messages;
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

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.jwtExpirationMs}")
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
            logger.error(Messages.INVALID_JWT_SIGNATURE, e.getMessage());
        }
        catch (MalformedJwtException e) {
            logger.error(Messages.INVALID_JWT_TOKEN, e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error(Messages.TOKEN_EXPIRED, e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error(Messages.TOKEN_UNSUPPORTED, e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error(Messages.TOKEN_CLAIMS_EMPTY, e.getMessage());
        }

        return false;
    }
}
