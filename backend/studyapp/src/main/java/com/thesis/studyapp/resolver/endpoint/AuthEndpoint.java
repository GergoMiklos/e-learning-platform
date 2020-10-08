package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.LoginInputDto;
import com.thesis.studyapp.dto.RegisterInputDto;
import com.thesis.studyapp.dto.TokenDto;
import com.thesis.studyapp.security.annotation.NotAuthenticated;
import com.thesis.studyapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final AuthService authService;

    @NotAuthenticated
    public void register(RegisterInputDto input) {
        authService.register(input);
    }

    @NotAuthenticated
    public TokenDto login(LoginInputDto input) {
        return authService.login(input);
    }

    @NotAuthenticated
    public boolean isUsernameAlreadyRegistered(String username) {
        return authService.isUsernameAlreadyRegistered(username);
    }

}
