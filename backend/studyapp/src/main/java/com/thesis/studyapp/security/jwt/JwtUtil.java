package com.thesis.studyapp.security.jwt;

import com.thesis.studyapp.security.service.DefaultUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final int JWT_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer (.+?)$");

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final Environment environment;

    public String generateJwtToken(Authentication authentication) {
        String jwtSecret = getJwtSecret();

        DefaultUserDetails userPrincipal = (DefaultUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

//    public String refreshJwtToken(String token) {
//        String jwtSecret = getJwtSecret();
//
//        if (validateJwtToken(token)) {
//            String userName = getUserNameFromJwtToken(token);
//            return Jwts.builder()
//                    .setSubject(userName)
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
//                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                    .compact();
//        } else {
//            throw new InvalidTokenException("Invalid token");
//        }
//    }

    public String getUserNameFromJwtToken(String token) {
        String jwtSecret = getJwtSecret();
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        String jwtSecret = getJwtSecret();

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getJwtSecret() {
        String jwtSecret = environment.getProperty("JWT_SECRET");
        if (jwtSecret != null) {
            return jwtSecret;
        } else {
            throw new IllegalStateException("JWT_SECRET is not configured");
        }
    }

    public String parseJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            return null;
        }
    }
}
