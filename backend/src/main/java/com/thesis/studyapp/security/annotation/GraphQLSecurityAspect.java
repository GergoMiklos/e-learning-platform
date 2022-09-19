package com.thesis.studyapp.security.annotation;


import com.thesis.studyapp.exception.UnauthorizedException;
import com.thesis.studyapp.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//WARNING! This aspect works only from the original request thread
@Component
@Aspect
@Order(1)
@RequiredArgsConstructor
public class GraphQLSecurityAspect {

    private final AuthenticationUtil authenticationUtil;
    private final Logger logger = LoggerFactory.getLogger(GraphQLSecurityAspect.class);

    /**
     * Methods with @Authenticated annotation can be called only by authenticated user.
     * The particular methods are defined in the Pointcut expression.
     */
    @Before("authenticated()")
    public void isAuthenticated() {
        if (!authenticationUtil.isAuthenticated()) {
            logger.error("Unauthorized request prohibited");
            throw new UnauthorizedException("Not authenticated");
        }
    }

    /**
     *  Methods with @Authenticated annotation will be included to security check
     */
    @Pointcut("@annotation(com.thesis.studyapp.security.annotation.Authenticated)")
    private void authenticated() {
    }

}
