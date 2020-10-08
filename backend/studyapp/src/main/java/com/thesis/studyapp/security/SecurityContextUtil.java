package com.thesis.studyapp.security;

import com.thesis.studyapp.exception.UnauthorizedException;
import com.thesis.studyapp.security.service.DefaultUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityContextUtil.class);

    public DefaultUserDetails getPrincipals() {
        try {
            if (isAuthenticated()) {
                return (DefaultUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            } else {
                logger.error("Unauthorized request prohibited");
                throw new UnauthorizedException("Not authenticated");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Security context error");
        }
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}

